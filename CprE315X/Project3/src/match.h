/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Match Programming Assignment
 */
#ifndef MATCH_H
#define MATCH_H

typedef struct person {
	int id;
	vector<struct person> * matches;
} person;

#endif