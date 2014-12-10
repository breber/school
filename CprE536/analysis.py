import os
import string
import sys

# statistical analysis
import pandas as pd

def process_files(file_list, avg_file_name):
    histograms = []
    all_data = []

    for filename in file_list:
        contents = read_file(filename)
        if avg_file_name:
            all_data.extend(contents)

        #print("%s --> %s" % (filename, [ "0x%02x" % c for c in contents ]))

        s = gen_hist(contents, "%s_hist.png" % filename)

        print(filename)
        print("\tText Likelihood : %f" % text_likelihood(s))
        print("\tImage Likelihood: %f (%d)" % jpeg_likelihood(s))
        print("\tPDF Likelihood  : %d" % pdf_likelihood(s))

    if avg_file_name:
        return gen_hist(all_data, avg_file_name)

def gen_hist(contents, out_file_name):
    import matplotlib.pyplot as plt

    # sources
    # http://matplotlib.org/users/pyplot_tutorial.html
    # http://pandas.pydata.org/pandas-docs/dev/generated/pandas.Series.html
    # http://pandas.pydata.org/pandas-docs/stable/10min.html
    # http://digitalcorpora.org/corp/nps/files/govdocs1/

    # Generate a series given the contents of the file,
    # and use that to create a histogram with relevant
    # statistical information
    s = pd.Series(contents)
    hist = s.hist(bins=8, normed=True, histtype='bar', range=(0, 256), stacked=False, color=['crimson'])
    fig = hist.get_figure()
    plt.xlim(0, 256)
    plt.ylim(0, .01)

    quantiles = s.quantile([.25, .5, .75])
    first = quantiles.iget(0)
    second = quantiles.iget(1)
    third = quantiles.iget(2)
    iqr = third - first
    outlier_range = 1.5 * iqr

    plt.xlabel("mean: %d, iqr: %d, qtile: (%d (%d, %d, %d) %d)" % (s.mean(), iqr, first - outlier_range, first, second, third, third + outlier_range))
    fig.savefig(out_file_name)

    # matplotlib keeps figures open until they are explicitly
    # closed which can cause weird behavior when building multiple
    # plots in a row. So just close the figure here, and clear the
    # stored figures and axes
    plt.close('all')
    plt.clf()
    plt.cla()

    # return the series so that it can be used for comparison purposes
    return s

def text_likelihood(s):
    lowest_ascii = 32
    highest_ascii = 126

    text_likelihood = 0
    num_tests = 0

    # Go through all the values in the contents of the
    # file and convert them to characters and see if they
    # are deemed "printable"
    num_printable = 0
    for c in s:
        if chr(c) in string.printable:
            num_printable += 1

    text_likelihood += 1 if (num_printable > (.75 * s.size)) else 0
    num_tests += 1

    # Check if the mean is in the ascii text range
    mean = s.mean()
    text_likelihood += 1 if (lowest_ascii <= mean <= highest_ascii) else 0
    num_tests += 1

    # Check if the median is in the ascii text range
    median = s.median()
    text_likelihood += 1 if (lowest_ascii <= median <= highest_ascii) else 0
    num_tests += 1

    return text_likelihood / float(num_tests)

def jpeg_likelihood(s):
    likelihood = 0
    num_tests = 0

    # check if it starts with the JPEG file signature
    first = s.iget(0)
    second = s.iget(1)
    third = s.iget(2)
    likelihood += 5 if (first == 0xFF and second == 0xD8 and third == 0xFF) else 0
    num_tests += 5

    # check if it has many 0xFF bytes followed by 0x00
    # this is a common pattern in JPEG files
    # see http://www.dfrws.org/2013/proceedings/DFRWS2013-8.pdf
    consecutive = 0
    for i in range(0, s.size - 1):
        if s.iget(i) == 0xFF and s.iget(i + 1) == 0x00:
            consecutive += 1

    # Given the block size for the assignment is 512, these
    # numbers make sense. For different block sizes (or full files),
    # these will likely need to be updated
    if consecutive >= 5:
        likelihood += 5
    elif consecutive >= 4:
        likelihood += 4
    elif consecutive >= 3:
        likelihood += 3
    elif consecutive >= 2:
        likelihood += 2
    elif consecutive >= 1:
        likelihood += 1

    num_tests += 3

    # As far as histograms go, the histogram for JPEG is generally pretty
    # uniform. There aren't really any large spikes at any specific locations
    counts = s.value_counts(normalize=True, bins=8)
    hist_mean = counts.mean()
    # print("\t\thist_mean: %f" % hist_mean)
    for val in counts.tolist():
        # print("\t\t\t%f <= %f <= %f" % (hist_mean - .25 * hist_mean, val, hist_mean + .25 * hist_mean))
        if hist_mean - .25 * hist_mean <= val <= hist_mean + .25 * hist_mean:
            likelihood += 1
        num_tests += 1

    return (likelihood / float(num_tests), consecutive)

def pdf_likelihood(s):
    likelihood = 0
    num_tests = 0

    # check if it starts with the PDF file signature
    # http://www.garykessler.net/library/file_sigs.html
    first = s.iget(0)
    second = s.iget(1)
    third = s.iget(2)
    fourth = s.iget(3)
    likelihood += 5 if (first == 0x25 and second == 0x50 and third == 0x44 and fourth == 0x46) else 0
    num_tests += 5

    # For histograms, PDF tends to have a spike around the ascii character range,
    # with relatively uniform for the rest of the range


    return likelihood / float(num_tests)

def read_file(filename):
    import struct
    contents = []

    # Read the file byte by byte and return a list
    # of the byte values (as integers)
    with open(filename, "rb") as f:
        byte = f.read(1)
        while byte:
            contents.append(struct.unpack('B', byte)[0])
            byte = f.read(1)

    return contents

def main():
    data = [
        {
            "input": "input/JPEG/",
            "hist": None,
            "known": True
        },
        {
            "input": "input/PDF/",
            "hist": None,
            "known": True
        },
        {
            "input": "input/WORD/",
            "hist": None,
            "known": True
        },
        {
            "input": "input/Assignment/",
            "hist": None,
            "known": False
        },
        {
            "input": "/Users/breber/Downloads/000/JPEG/",
            "hist": None,
            "known": True
        },
        {
            "input": "/Users/breber/Downloads/000/PDF/",
            "hist": None,
            "known": True
        },
        {
            "input": "/Users/breber/Downloads/000/TXT/",
            "hist": None,
            "known": True
        },
        {
            "input": "/Users/breber/Downloads/000/WORD/",
            "hist": None,
            "known": True
        }
    ]

    for item in data:
        input_path = item['input']
        print("Processing: %s" % input_path)

        # build a list of files in the given directory
        from os.path import isfile, join
        file_list = [ join(input_path, f) for f in os.listdir(input_path) if isfile(join(input_path, f)) ]

        # read all of the files
        if item['known']:
            avg_file_name = join(input_path, 'avg_hist.png')
        else:
            avg_file_name = None

        item['hist'] = process_files(file_list, avg_file_name)

if __name__ == "__main__":
    main()
