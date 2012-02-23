/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Union-Find Implementation
 */
#include "unionfind.h"

/** 
 * Create the Union-Find Datastructure
 */
void unionfind::makeUnionFind(nodelist & nodeList) {
	// Create the array of nodes
	list = new int[nodeList.numNodes];
	numNodes = nodeList.numNodes;

	for (int i = 0; i < nodeList.numNodes; i++) {
		list[i] = i;
	}
}

/** 
 * Perform a Find operation
 */
int unionfind::find(int nodeNum) {
	return list[nodeNum];
}

/** 
 * Perform a Union operation
 */
void unionfind::unionize(int node1Num, int node2Num) {
	int fromSet = list[node1Num];
	int toSet = list[node2Num];

	for (int i = 0; i < numNodes; i++) {
		if (list[i] == fromSet) {
			list[i] = toSet;
		}
	}
}
