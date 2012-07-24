#include <iostream>

using namespace std;

int main() {
	string message;
	
	// Wait for message on CIN
	do {
		cin >> message;
	
		cerr << "Got from CIN " << message << " DONE" << endl;
	
//		// Print out the received message on COUT
		cout << "FROMPILL_" << message << endl;
	} while (message.find("stop") == string::npos);
	
	cerr << "STOP" << endl;
	
	return 0;
}

