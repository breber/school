# Brian Reber (breber@iastate.edu)
# CprE 310, Fall 2011
# Random Graph Generator

# Creates nNodes number of nodes, with nLinks links
# between nodes. Writes out to stdout - pipe output
# to a file that can be passed in to PageRank executable.

import random

nLinks = 500
nNodes = 500

for i in range(0, nLinks):
	print str(random.randint(0, nNodes)) + "\t" + str(random.randint(0, nNodes))