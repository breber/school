/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Spell Check Programming Assignment
 */
#ifndef SPELLCHECK_H
#define SPELLCHECK_H

#include <string>

int sequenceAlignment(std::string &, std::string &, int ** data = NULL);
int getPenalty(char, char);
void reconstructAlignment(std::string &, std::string &, int **, int, int);

#endif
