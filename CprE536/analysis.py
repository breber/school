import os
import string
import sys

# statistical analysis
import pandas as pd

def process_files(file_list, avg_file_name):
    histograms = []
    all_data = []

    text_files = []

    for filename in file_list:
        contents = read_file(filename)
        if avg_file_name:
            all_data.extend(contents)

        #print("%s --> %s" % (filename, [ "0x%02x" % c for c in contents ]))

        s = gen_hist(contents, "%s_hist.png" % filename)

        print(filename)
        text_prob = text_likelihood(s)
        print("\tText Likelihood : %f" % text_prob)
        print("\tImage Likelihood: %f" % jpeg_likelihood(s))
        print("\tPDF Likelihood  : %f" % pdf_likelihood(s))

        if text_prob > .75:
            text_files.append((filename, contents))

    if avg_file_name:
        full_hist = gen_hist(all_data, avg_file_name)
    else:
        full_hist = None

    return (full_hist, text_files)

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

    # Check if all the values are 0. This could be an empty text file, so we'll
    # give it a point for likelihood, but it probably isn't text
    if s.min() == 0 and s.max() == 0:
        text_likelihood += 1

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
    try:
        counts = s.value_counts(normalize=True, bins=8)
        hist_mean = counts.mean()
        for val in counts.tolist():
            if .75 * hist_mean <= val <= 1.25 * hist_mean:
                likelihood += 1
            num_tests += 1
    except:
        # we only get here if all the values are the same, in which case
        # it isn't likely this is a JPEG
        num_tests += 8
        pass

    return likelihood / float(num_tests)

def pdf_likelihood(s):
    likelihood = 0
    num_tests = 0

    # check if it starts with the PDF file signature
    # http://www.garykessler.net/library/file_sigs.html
    first = s.iget(0)
    second = s.iget(1)
    third = s.iget(2)
    fourth = s.iget(3)
    likelihood += 10 if (first == 0x25 and second == 0x50 and third == 0x44 and fourth == 0x46) else 0
    num_tests += 5

    # For histograms, PDF tends to have a spike around the ascii character range,
    # with relatively uniform for the rest of the range
    # [ normal, spike, normal + a bit, spike, normal, normal, normal, normal]
    try:
        counts = s.value_counts(normalize=True, bins=8)
        normal_sections = pd.Series([counts.iget(0), counts.iget(4), counts.iget(5), counts.iget(6), counts.iget(7)])
        avg = normal_sections.mean()

        if counts.iget(1) > 1.1 * avg:
            likelihood += 3
        if counts.iget(3) > 1.1 * avg:
            likelihood += 3

        num_tests += 6
    except:
        # we only get here if all the values are the same, in which case
        # it could be a PDF, but it is hard to say for sure
        likelihood += 1
        num_tests += 1


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

def stitch_text(data):
    # Read in the dictionary - this should work on Unix-based machines
    # Change th path to a plaintext dictionary if that file doesn't exist
    dictionary = []
    with open('/usr/share/dict/words', 'r') as f:
        word = f.readline()
        while word:
            dictionary.append(word[0:-1])
            word = f.readline()

    # Go through the files listed as text
    text_files = []
    for item in data:
        for item2 in item['text']:
            text_files.append({ 'filename': item2[0], 'contents': item2[1] })

    # find the first and last partial words in each block
    for file in text_files:
        file['char_contents'] = [chr(x) for x in file['contents']]
        contents = file['char_contents']

        # Only check if we have at least one ' ' character
        if ' ' in contents:
            file['first'] = contents[0:contents.index(' ')]
            file['first'] = filter(lambda x: x in string.printable, file['first'])
            contents.reverse()

            file['last'] = contents[0:contents.index(' ')]
            contents.reverse()
            file['last'].reverse()
            file['last'] = filter(lambda x: x in string.printable, file['last'])

    for i in range(0, len(text_files)):
        for j in range(i + 1, len(text_files)):
            test1 = "%s%s" % (text_files[i]['last'], text_files[j]['first'])
            test2 = "%s%s" % (text_files[j]['last'], text_files[i]['first'])
            if test1 in dictionary:
                print("\tfound word1: [%d][%d] %s" % (test1, i, j))
            if test2 in dictionary:
                print("\tfound word2: [%d][%d] %s" % (test2, j, i))


def main():
    data = [
        {
            "input": "input/JPEG/",
            "hist": None,
            "known": True,
            "text": None
        },
        {
            "input": "input/PDF/",
            "hist": None,
            "known": True,
            "text": None
        },
        {
            "input": "input/WORD/",
            "hist": None,
            "known": True,
            "text": None
        },
        {
            "input": "input/Assignment/",
            "hist": None,
            "known": False,
            "text": None
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

        item['hist'], item['text'] = process_files(file_list, avg_file_name)

    # Do some post processing to try and stich some files back together
    # For a first shot, try just looking at text files
    stitch_text(data)

if __name__ == "__main__":
    main()
