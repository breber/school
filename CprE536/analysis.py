import os
import string
import sys

# statistical analysis
import pandas as pd

def process_files(file_list, avg_file_name):
    histograms = []

    for filename in file_list:
        contents = read_file(filename)

        #print("%s --> %s" % (filename, [ "0x%02x" % c for c in contents ]))

        s = gen_hist(contents, "%s_hist.png" % filename)

        count_series = s.value_counts(normalize=True)

        items = {}
        for x, y in count_series.iteritems():
            items[x] = y

        histograms.append(items)

        # if is_text(contents):
        #     print("likely text")
        # else:
        #     print("unknown")


    avg_hist = {}
    for i in range(0, 255):
        total = 0

        for hist in histograms:
            if i in hist:
                total += hist[i]

        avg_hist[i] = total / len(histograms)

    return gen_hist(avg_hist, avg_file_name)

def gen_hist(contents, out_file_name):
    import matplotlib.pyplot as plt
    s = pd.Series(contents)
    hist = s.hist(bins=16, normed=True, histtype='bar', range=(0, 256), stacked=False, color=['crimson'])
    fig = hist.get_figure()
    fig.savefig(out_file_name)
    plt.close('all')
    plt.clf()
    plt.cla()

    return s

def is_text(contents):
    num_printable = 0

    for c in contents:
        if chr(c) in string.printable:
            num_printable += 1

    return num_printable > (.75 * len(contents))

def read_file(filename):
    import struct
    contents = []

    with open(filename, "rb") as f:
        byte = f.read(1)
        while byte:
            contents.append(struct.unpack('B', byte)[0])
            byte = f.read(1)

    return contents

def main():
    if len(sys.argv) < 2:
        return

    input_path = sys.argv[1]

    # build a list of files in the given directory
    from os.path import isfile, join
    file_list = [ join(input_path, f) for f in os.listdir(input_path) if isfile(join(input_path, f)) ]

    # read all of the files
    process_files(file_list, join(input_path, 'avg_hist.png'))

if __name__ == "__main__":
    main()
