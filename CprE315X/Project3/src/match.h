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
	int id;
	int fromNode;
	int toNode;
	int capacity;
	int flow;
} edge;

typedef struct {
	int value;
	edge * theEdge;
} pathnode;

typedef struct {
	int index;
	std::vector<edge *> * edges;
} queuednode;

int parseData(std::vector< std::vector<edge *> * > *, int, int, std::fstream &);

std::vector<pathnode *> * findPath(std::vector< std::vector<edge *> * > *, int, int);

bool compareEdges(edge *, edge *);

void fordFulkerson(std::vector< std::vector<edge *> * > *, int, int, int);

#endif
