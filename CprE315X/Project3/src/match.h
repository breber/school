/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Match Programming Assignment
 */
#ifndef MATCH_H
#define MATCH_H

#include <vector>
#include <fstream>

typedef struct {
	int fromNode;
	int toNode;
	int capacity;
} edge;

void parseData(std::vector< std::vector<edge *> * > *, int, int, std::fstream &);

#endif
