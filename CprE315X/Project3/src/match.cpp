/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Match Programming Assignment
 */
#include <iostream>
#include "match.h"

using namespace std;

int main(int argc, char ** argv) {
	fstream inputfile;
	
	vector< vector<edge *> * > * nodes = new vector< vector<edge *> * >;;
	
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

	parseData(nodes, numGuys, numGals, inputfile);

	cout << "Num Guys: " << numGuys << endl;
	cout << "Num Gals: " << numGals << endl;

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
	for (int i = 0; i < numGuys; i++) {
		// Add the edge from source to the guy
		edge * forward = new edge;
		forward->fromNode = 0;
		forward->toNode = i;
		forward->capacity = 1;
		nodes->at(0)->push_back(forward);
		
		// Add the reverse edge
		edge * reverse = new edge;
		reverse->fromNode = i;
		reverse->toNode = 0;
		reverse->capacity = 0;
		nodes->at(i)->push_back(reverse);
	}
	
	// Add the edges from the gals to the sink
	for (int i = 0; i < numGals; i++) {
		// Add the edge from gal to the sink
		edge * forward = new edge;
		forward->fromNode = i;
		forward->toNode = nodes->size() - 1;
		forward->capacity = 1;
		nodes->at(i)->push_back(forward);
		
		// Add the reverse edge
		edge * reverse = new edge;
		reverse->fromNode = nodes->size() - 1;
		reverse->toNode = i;
		reverse->capacity = 0;
		nodes->at(nodes->size() - 1)->push_back(reverse);
	}

	// Read in the comparison file
	while (!inputfile.eof()) {
		int guy;
		int gal;
		inputfile >> guy;
		inputfile >> gal;
		
		// Add the edge from guy to gal
		edge * forward = new edge;
		forward->fromNode = guy;
		forward->toNode = gal;
		forward->capacity = INT_MAX;
		nodes->at(guy)->push_back(forward);
		
		// Add the reverse edge
		edge * reverse = new edge;
		reverse->fromNode = guy;
		reverse->toNode = gal;
		reverse->capacity = 0;
		nodes->at(gal)->push_back(reverse);
	}
}