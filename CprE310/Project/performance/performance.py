# Brian Reber (breber@iastate.edu)
# CprE 310, Fall 2011
# Performance Testing Script

import os
import time

# Small Number of nodes, Small number of Links
print "Small Nodes, Small Edges"
startTime = time.time()
os.system("java -jar PageRank.jar -f data_slinks_snodes.txt > /dev/null")
print time.time() - startTime

# Small Number of nodes, Large number of Links
print "Small Nodes, Large Edges"
startTime = time.time()
os.system("java -jar PageRank.jar -f data_llinks_snodes.txt > /dev/null")
print time.time() - startTime

# Large Number of nodes, Small number of Links
print "Large Nodes, Small Edges"
startTime = time.time()
os.system("java -jar PageRank.jar -f data_slinks_lnodes.txt > /dev/null")
print time.time() - startTime

# Large Number of nodes, Large number of Links
print "Large Nodes, Large Edges"
startTime = time.time()
os.system("java -jar PageRank.jar -f data_llinks_lnodes.txt > /dev/null")
print time.time() - startTime
