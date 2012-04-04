/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Union-Find Header
 */
#ifndef UNION_FIND_H
#define UNION_FIND_H

#include "nodes.h"

class unionfind {
	int numNodes;
	int * list;
	public:
		void makeUnionFind(nodelist & nodeList);
		int find(int nodeNum);
		void unionize(int node1Num, int node2Num);  
};

#endif
