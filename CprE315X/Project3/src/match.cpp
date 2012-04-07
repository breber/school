/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Match Programming Assignment
 */
#include <iostream>
#include "match.h"

#define DEBUG

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

	cout << "Num Guys: " << numGuys << endl;
	cout << "Num Gals: " << numGals << endl;


#ifdef DEBUG
	// Print out the edges for each node
	for (int i = 0; i < nodes->size(); i++) {
		cout << "Node " << i << endl;
		vector<edge *> * cur = nodes->at(i);
		for (int j = 0; j < cur->size(); j++) {
			cout << "\t" << cur->at(j)->fromNode << " --> " << cur->at(j)->toNode << " with " << cur->at(j)->flow << " / " << cur->at(j)->capacity << endl;
		}
	}
#endif	

	vector<pathnode *> path;
	findPath(nodes, 0, nodes->size() - 1, path);
	
	cout << "found path" << endl;
	
	for (int i = 0; i < path.size(); i++) {
		cout << i << ": " << path[i]->theEdge->fromNode << " --> " << path[i]->theEdge->toNode << " with " << path[i]->theEdge->flow << " / " << path[i]->theEdge->capacity << endl;
	}

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
		reverse->fromNode = guy;
		reverse->toNode = gal;
		reverse->capacity = 0;
		reverse->flow = 0;
		nodes->at(gal)->push_back(reverse);
	}
}



vector<pathnode *> * findPath(vector< vector<edge *> * > * nodes, int fromNode, 
								int toNode, vector<pathnode *> & path) {
	cout << "findPath(" << fromNode << ", " << toNode << ", " << path.size() << ");" << endl;
								
	// We are currently at our destination, so return the path
	// we have built up to this point
	if (fromNode == toNode) {
		return &path;
	}
	
	// Find all of the edges coming out of our source node, and
	// iterate through them, trying to find a forward edge that
	// still has capacity remaining
	vector<edge *> * sourceEdges = nodes->at(fromNode);
	for (int i = 0; i < sourceEdges->size(); i++) {
		// Get the current edge to look at, and
		// find the residual value (since we don't store it)
		edge * cur = sourceEdges->at(i);
		int residualValue = cur->capacity - cur->flow;
		
		// If there is still space for us to flow forward,
		// continue investigating this path
		if (residualValue > 0) {
			bool contains = false;
			pathnode * temp = new pathnode;
			temp->value = residualValue;
			temp->theEdge = cur;
			
			cout << cur->fromNode << " --> " << cur->toNode << ": " << residualValue << " / " << cur->capacity << endl;
			
			for (int j = 0; j < path.size(); j++) {
				if ((path[j]->value == residualValue) && compareEdges(path[j]->theEdge, cur)) {
					contains = true;
					break;
				}
			}
			
			cout << "Contains? " << contains << endl;
			
			vector<pathnode *> * currentPath;
			// If we don't already have this edge in our path, add it and find a path from
			// the current node to the toNode
			if (!contains) {
				path.push_back(temp);
				currentPath = findPath(nodes, cur->toNode, toNode, path);
			}
			
			if (currentPath != NULL) {
				// We recursively found a path from this node to another
				// so return that path
				return currentPath;
			} else {
				// We didn't find a path, so remove the previously added node
				// so we can continue looking
				path.pop_back();
			}
		}
	}
	
	return NULL;
}

bool compareEdges(edge * edge1, edge * edge2) {
	bool from = (edge1->fromNode == edge2->fromNode);
	bool to = (edge1->toNode == edge2->toNode);
	bool cap = (edge1->capacity == edge2->capacity);
	
	return from && to && cap;
}