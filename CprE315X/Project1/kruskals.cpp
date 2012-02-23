/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Kruskal's Algorithm Implementation
 */
#include <iostream>
#include <fstream>
#include <list>
#include "nodes.h"
#include "unionfind.h"

using namespace std;

void doKruskals(nodelist &);

double diffclock(clock_t clock1, clock_t clock2) {
	double diffticks = clock1 - clock2;
	double diffms = (diffticks * 1000) / CLOCKS_PER_SEC;
	return diffms / 1000;
}

int main(int argc, char ** argv) {
	nodelist nodeList;

	// Read the nodes from cin into our nodeList struct
	readNodes(nodeList, false);

	clock_t start = clock();
	// Perform Kruskals's Algorithm
	doKruskals(nodeList);

	clock_t end = clock();
	cout << "Algorithm Runtime: " << double(diffclock(end, start)) << endl;
}

/**
 * Compare two nodes for sorting a list
 */
bool compareNodes(edge * one, edge * two) {
	return (one->weight < two->weight);
}

/**
 * Perform the Kruskals Algorithm on this list of nodes
 */
void doKruskals(nodelist & nodeList) {
	// Initialize an empty "priority queue"
	list<edge *> * pQueue = new list<edge *>();
	list<edge *> closedSet = list<edge *>();

	unionfind uf = unionfind();
	uf.makeUnionFind(nodeList);

	for (int i = 0; i < nodeList.numNodes; i++) {
		node * current = nodeList.nodes->at(i);
		for (int j = 0; j < current->edges->size(); j++) {
			pQueue->push_back(current->edges->at(j));
		}
	}

	// Sort our "priority queue"
	pQueue->sort(compareNodes);

	// Go through all the edges in the entire graph, from least to greatest
	while (!pQueue->empty()) {
		edge * e = pQueue->front();
		pQueue->pop_front();

		// Perform a find operation in our union-find datastructure
		int set1 = uf.find(e->fromNode);
		int set2 = uf.find(e->toNode);

		// If the two nodes are not in the same set, add the edge
		// to the closed set, and perform the Union operation
		// on the two sets
		if (set1 != set2) {
			closedSet.push_back(e);
			uf.unionize(set1, set2);
		}
	}

	// FINISH: Output results
	fstream file("output.txt", fstream::out);
	file << nodeList.numNodes << endl;
	file << closedSet.size() << endl;

	double totalWeight = 0;
	while (!closedSet.empty()) {
		edge * e = closedSet.front();
		closedSet.pop_front();
		totalWeight += e->weight;

		// Output to the file (making nodes 1 indexed instead of 0 indexed
		file << (e->fromNode + 1) << " " << (e->toNode + 1) << " " << e->weight << endl;
	}

	cout << "Total Weight: " << totalWeight << endl;
}
