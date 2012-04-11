/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Match Programming Assignment
 */
#include <iostream>
#include <queue>
#include <sstream>
#include "match.h"

using namespace std;

double diffclock(clock_t clock1, clock_t clock2) {
	double diffticks = clock1 - clock2;
	double diffms = (diffticks * 1000) / CLOCKS_PER_SEC;
	return diffms / 1000;
}

int main(int argc, char ** argv) {
	fstream inputfile;
	fstream outputfile;
	int numGuys = 0;
	int numGals = 0;
	
	vector< vector<edge *> * > * nodes = new vector< vector<edge *> * >;
	
	// Parse the command line argument
	if (argc != 3) {
		cout << "Wrong number of arguments" << endl;
		cout << "\t./match inputfile outputfile" << endl;
		
		return 0;
	} else {
		inputfile.open(argv[1], fstream::in);
		outputfile.open(argv[2], fstream::out);
	}
	
	// Get the number of guys and gals from the first two lines
	inputfile >> numGuys;
	inputfile >> numGals;

	// Parse the bulk of the data
	parseData(nodes, numGuys, numGals, inputfile);

	// Start timing the algorithm
	clock_t start = clock();

	// Perform the Ford Fulkerson Algorithm
	fordFulkerson(nodes, 0, nodes->size() - 1);
	
	// Buffer the output so that we can count the number of connections
	stringstream output;
	int numMatches = 0;
	
	// Print out the edges for each node
	for (int i = 1; i <= numGuys; i++) {
		vector<edge *> * cur = nodes->at(i);
		for (int j = 0; j < cur->size(); j++) {
			if (cur->at(j)->flow == 1) {
				numMatches++;
				output << cur->at(j)->fromNode << " " << (cur->at(j)->toNode - numGuys) << endl;
			}
		}
	}
	
	clock_t end = clock();
	cout << "Algorithm Runtime: " << double(diffclock(end, start)) << " seconds" << endl;
	cout << "See " << argv[2] << " for the matching results" << endl;

	// Print out the number of connections, and then the buffered output
	outputfile << numMatches << endl;
	outputfile << output.str() << endl;

	return 0;
}

/**
 * Parse the data from the given file, and put the nodes in their
 * respective locations in the nodes vector.
 */
void parseData(vector< vector<edge *> * > * nodes, int numGuys, int numGals, fstream & inputfile) {
	// Add a vector of edges for the source node
	vector<edge *> * source = new vector<edge *>;
	nodes->push_back(source);
	
	// Add a vector of edges for all guys and gals
	for (int i = 0; i < numGuys + numGals; i++) {
		vector<edge *> * temp = new vector<edge *>;
		nodes->push_back(temp);
	}

	// Add a vector of edges for the sink node
	vector<edge *> * sink = new vector<edge *>;
	nodes->push_back(sink);

	// Add the edges from the source to the guys
	for (int i = 1; i <= numGuys; i++) {
		// Add the edge from source to the guy
		edge * forward = new edge;
		forward->fromNode = 0;
		forward->toNode = i;
		forward->capacity = 1;
		forward->flow = 0;
		nodes->at(0)->push_back(forward);
		
		// Add the reverse edge
		edge * reverse = new edge;
		reverse->fromNode = i;
		reverse->toNode = 0;
		reverse->capacity = 0;
		reverse->flow = 0;
		nodes->at(i)->push_back(reverse);
	}

	// Add the edges from the gals to the sink
	for (int i = numGuys + 1; i <= numGuys + numGals; i++) {
		// Add the edge from gal to the sink
		edge * forward = new edge;
		forward->fromNode = i;
		forward->toNode = nodes->size() - 1;
		forward->capacity = 1;
		forward->flow = 0;
		nodes->at(i)->push_back(forward);
		
		// Add the reverse edge
		edge * reverse = new edge;
		reverse->fromNode = nodes->size() - 1;
		reverse->toNode = i;
		reverse->capacity = 0;
		reverse->flow = 0;
		nodes->at(nodes->size() - 1)->push_back(reverse);
	}

	// Read in the comparison file
	while (!inputfile.eof()) {
		int guy;
		int gal;
		inputfile >> dec >> guy;
		inputfile >> dec >> gal;

		// If we weren't able to read the input as an int,
		// break out of this loop
		if (inputfile.fail()) {
			continue;
		}

		// So each node has a unique id, gals will be given the id
		// numGuys + gal
		gal += numGuys;

		// Add the edge from guy to gal
		edge * forward = new edge;
		forward->fromNode = guy;
		forward->toNode = gal;
		forward->capacity = INT_MAX;
		forward->flow = 0;
		nodes->at(guy)->push_back(forward);

		// Add the reverse edge
		edge * reverse = new edge;
		reverse->fromNode = gal;
		reverse->toNode = guy;
		reverse->capacity = 0;
		reverse->flow = 0;
		nodes->at(gal)->push_back(reverse);
	}

	inputfile.close();
}

/**
 * Find a path from fromNode to toNode using BFS.
 * Returns the path, with the associated flow.
 */
vector<pathnode *> * findPath(vector< vector<edge *> * > * nodes, int fromNode, int toNode) {
	// The actual path to return
	vector<int> p(nodes->size(), -1);
	p[fromNode] = -2;

	// When we visit an edge, mark it as visited
	vector<int> marked(nodes->size(), 0);
	marked[fromNode] = INT_MAX;
	
	// Our queue for BFS
	queue<int> q;
	
	// Enqueue source node
	q.push(fromNode);
	
	// While we don't have elements in the queue to process
	while (!q.empty()) {
		int currentNode = q.front();
		q.pop();

		// Iterate through all the edges for the current node
		vector<edge *> * edges = nodes->at(currentNode);
		for (int i = 0; i < edges->size(); i++) {
			edge * e = edges->at(i);
			int edgeFlow = e->capacity - e->flow;

			// If the current edge hasn't been used before, and still has flow
			// available to use, look into it
			if ((p[e->toNode] == -1) && (edgeFlow > 0)) {
				p[e->toNode] = currentNode;
				marked[e->toNode] = (marked[e->fromNode] > edgeFlow) ? edgeFlow : marked[e->fromNode];
				
				// If this isn't the node we are looking for,
				// add the connected node to our queue to process
				if (e->toNode != toNode) {
					q.push(e->toNode);
				} else {
					// We found a path...rebuild path vector...
					vector<pathnode *> * path = new vector<pathnode *>;
					int var = toNode;

					// Build the solution from the sink to the source
					while (var != fromNode) {
						// The marked array has the flow values
						pathnode * tmp = new pathnode;
						tmp->value = marked[var];
						
						// Find the actual edge pointer that corresponds with
						// the connection from the previous node to this node
						int from = p[var];
						vector<edge *> * tmpEdges = nodes->at(from);
						for (int j = 0; j < tmpEdges->size(); j++) {
							if (tmpEdges->at(j)->toNode == var) {
								tmp->theEdge = tmpEdges->at(j);
							}
						}
						
						// Add it to the beginning of the path
						path->insert(path->begin(), tmp);
						
						// Update var to the current edge's source
						var = tmp->theEdge->fromNode;
					}
					
					return path;
				}
			}
		}
	}
	
	return NULL;
}

/**
 * Perform the Ford-Fulkerson algorithm on the given graph
 */
void fordFulkerson(vector< vector<edge *> * > * nodes, int fromNode, int toNode) {
	vector<pathnode *> * augmentingPath = findPath(nodes, 0, nodes->size() - 1);
	
	// While we actually have an augmenting path, process
	while (augmentingPath != NULL && augmentingPath->size() > 0) {
		// Find the max flow we can push through the augmenting path
		int minResidual = INT_MAX;
		for (int i = 0; i < augmentingPath->size(); i++) {
			pathnode * cur = augmentingPath->at(i);
			if (cur->value < minResidual) {
				minResidual = cur->value;
			}
		}

		// Go through and update the flow with the minResidual value
		for (int i = 0; i < augmentingPath->size(); i++) {
			pathnode * cur = augmentingPath->at(i);

			// Update the flow on this edge
			cur->theEdge->flow += minResidual;

			// Get the edges leaving the toNode of this edge
			// so that we can update the residual flow as well
			vector<edge *> * temp = nodes->at(cur->theEdge->toNode);
			for (int j = 0; j < temp->size(); j++) {
				if (temp->at(j)->toNode == cur->theEdge->fromNode) {
					temp->at(j)->flow -= minResidual;
					break;
				}
			}
		}

		// Clear the augmenting path, and find a new path
		while (!augmentingPath->empty()) {
			pathnode * current = augmentingPath->back();
			augmentingPath->pop_back();
			
			delete current;
		}

		augmentingPath = findPath(nodes, 0, nodes->size() - 1);
	}
}
