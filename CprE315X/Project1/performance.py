# Brian Reber (breber@iastate.edu)
# CprE 315X, Spring 2012
# Performance Testing Script

import os
import time

print "Prims"

# Sparse1
print "Sparse1"
startTime = time.time()
os.system("./prims < ../HW/prog01graphs/sparse1.txt")
os.system("mv output.txt prims_sparse1.txt")
print "Total Runtime: ", time.time() - startTime
print

# Sparse2
print "Sparse2"
startTime = time.time()
os.system("./prims < ../HW/prog01graphs/sparse2.txt")
os.system("mv output.txt prims_sparse2.txt")
print "Total Runtime: ", time.time() - startTime
print

# Sparse3
print "Sparse3"
startTime = time.time()
os.system("./prims < ../HW/prog01graphs/sparse3.txt")
os.system("mv output.txt prims_sparse3.txt")
print "Total Runtime: ", time.time() - startTime
print

# Dense1
print "Dense1"
startTime = time.time()
os.system("./prims < ../HW/prog01graphs/dense1.txt")
os.system("mv output.txt prims_dense1.txt")
print "Total Runtime: ", time.time() - startTime
print

# Dense2
print "Dense2"
startTime = time.time()
os.system("./prims < ../HW/prog01graphs/dense2.txt")
os.system("mv output.txt prims_dense2.txt")
print "Total Runtime: ", time.time() - startTime
print

# Dense3
print "Dense3"
startTime = time.time()
os.system("./prims < ../HW/prog01graphs/dense3.txt")
os.system("mv output.txt prims_dense3.txt")
print "Total Runtime: ", time.time() - startTime
print

# Kruskals
print
print "Kruskals"

# Sparse1
print "Sparse1"
startTime = time.time()
os.system("./kruskals < ../HW/prog01graphs/sparse1.txt")
os.system("mv output.txt kruskals_sparse1.txt")
print "Total Runtime: ", time.time() - startTime
print

# Sparse2
print "Sparse2"
startTime = time.time()
os.system("./kruskals < ../HW/prog01graphs/sparse2.txt")
os.system("mv output.txt kruskals_sparse2.txt")
print "Total Runtime: ", time.time() - startTime
print

# Sparse3
print "Sparse3"
startTime = time.time()
os.system("./kruskals < ../HW/prog01graphs/sparse3.txt")
os.system("mv output.txt kruskals_sparse3.txt")
print "Total Runtime: ", time.time() - startTime
print

# Dense1
print "Dense1"
startTime = time.time()
os.system("./kruskals < ../HW/prog01graphs/dense1.txt")
os.system("mv output.txt kruskals_dense1.txt")
print "Total Runtime: ", time.time() - startTime
print

# Dense2
print "Dense2"
startTime = time.time()
os.system("./kruskals < ../HW/prog01graphs/dense2.txt")
os.system("mv output.txt kruskals_dense2.txt")
print "Total Runtime: ", time.time() - startTime
print

# Dense3
print "Dense3"
startTime = time.time()
os.system("./kruskals < ../HW/prog01graphs/dense3.txt")
os.system("mv output.txt kruskals_dense3.txt")
print "Total Runtime: ", time.time() - startTime
print
