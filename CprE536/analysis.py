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

        # if is_text(contents):
        #     print("likely text")
        # else:
        #     print("unknown")

    if avg_file_name:
        return gen_hist(all_data, avg_file_name)

def gen_hist(contents, out_file_name):
    import matplotlib.pyplot as plt
    s = pd.Series(contents)
    hist = s.hist(bins=16, normed=True, histtype='bar', range=(0, 256), stacked=False, color=['crimson'])
    fig = hist.get_figure()
    plt.xlim(0, 256)
    fig.savefig(out_file_name)
    plt.close('all')
    plt.clf()
    plt.cla()

    return s

def is_text(contents):
    num_printable = 0

    # Go through all the values in the contents of the
    # file and convert them to characters and see if they
    # are deemed "printable"
    for c in contents:
        if chr(c) in string.printable:
            num_printable += 1

    return num_printable > (.75 * len(contents))

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
        }
    ]

    for item in data:
        input_path = item['input']

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
