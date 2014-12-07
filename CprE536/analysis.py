import os
import string
import sys

# statistical analysis
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

file_contents = {}

def process_files(file_list):
    for filename in file_list:
        contents = read_file(filename)

        s = pd.Series(contents)
        df = pd.DataFrame({filename : s})
        print(df.describe())

        if is_text(contents):
            print("likely text")
        else:
            print("unknown")


def is_text(contents):
    num_printable = 0

    for c in contents:
        if c in string.printable:
            num_printable += 1

    return num_printable > (.75 * len(contents))

def read_file(filename):
    contents = []

    with open(filename, "rb") as f:
        byte = f.read(1)
        while byte:
            contents.append(byte)
            byte = f.read(1)

    return contents

def main():
    if len(sys.argv) < 2:
        return

    # build a list of files in the given directory
    from os.path import isfile, join
    file_list = [ join(sys.argv[1], f) for f in os.listdir(sys.argv[1]) if isfile(join(sys.argv[1], f)) ]

    # read all of the files
    process_files(file_list)

if __name__ == "__main__":
    main()
