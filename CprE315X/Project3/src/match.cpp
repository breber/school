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

int main(int argc, char ** argv) {
	fstream inputfile;
	
	vector< vector<edge *> * > * nodes = new vector< vector<edge *> * >;
	vector<int> * flows = new vector<int>;
	
	int numGuys = 0;
	int numGals = 0;
	
	// Parse the command line argument
	if (argc != 2) {
		cout << "Wrong number of arguments" << endl;
		cout << "\t./match inputfile" << endl;
		
		return 0;
	} else {
		inputfile.open(argv[1], fstream::in);
	}
	
	// Get the number of guys and gals from the first two lines
	inputfile >> numGuys;
	inputfile >> numGals;

	// Parse the bulk of the data
	parseData(nodes, numGuys, numGals, inputfile);

	// Perform the Ford Fulkerson Algorithm
	fordFulkerson(nodes, 0, nodes->size() - 1);
	
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
	
	cout << numMatches << endl;
	cout << output.str() << endl;

	return 0;
}

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
		inputfile >> guy;
		inputfile >> gal;

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
}

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
	
	while (!q.empty()) {
		int currentNode = q.front();
		q.pop();

		vector<edge *> * edges = nodes->at(currentNode);

		for (int i = 0; i < edges->size(); i++) {
			edge * e = edges->at(i);
			int edgeFlow = e->capacity - e->flow;

			if ((p[e->toNode] == -1) && (edgeFlow > 0)) {
				p[e->toNode] = currentNode;
				marked[e->toNode] = (marked[e->fromNode] > edgeFlow) ? edgeFlow : marked[e->fromNode];
				
				if (e->toNode != toNode) {
					q.push(e->toNode);
				} else {
					// We found a path...rebuild path vector...
					vector<pathnode *> * path = new vector<pathnode *>;
					int var = toNode;

					while (var != fromNode) {
						pathnode * tmp = new pathnode;
						tmp->value = marked[var];
						
						int from = p[var];
						vector<edge *> * tmpEdges = nodes->at(from);
						for (int j = 0; j < tmpEdges->size(); j++) {
							if (tmpEdges->at(j)->toNode == var) {
								tmp->theEdge = tmpEdges->at(j);
							}
						}
						
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

void fordFulkerson(vector< vector<edge *> * > * nodes, int fromNode, int toNode) {
	vector<pathnode *> * augmentingPath = findPath(nodes, 0, nodes->size() - 1);
	
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
		augmentingPath->clear();
		// TODO: delete everything....
		augmentingPath = findPath(nodes, 0, nodes->size() - 1);
	}
}
