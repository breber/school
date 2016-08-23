#include <iostream>
#include <string>
#include <fstream>
#include <sstream>

int main(int argc, char *argv[]) {
	const std::string fileName("white.zip");

	std::ifstream infile("crackstation-human-only.txt");
	char ret[200];

	bool started = false;
	while (1) {
		std::string current;
		std::getline(infile, current);

		if (!started) {
			started = (current == "dkbvsog21");
			continue;
		}

		std::stringstream cmd;
		cmd << "unzip -P'" << current << "' -t " << fileName << " 2>&1";

		std::cout << cmd.str() << std::endl;

		FILE *pipe = popen(cmd.str().c_str(), "r");
		while (!feof(pipe)) {
			fgets((char*)&ret, 200, pipe);
			if (strcasestr(ret, "ok") != NULL) {
				std::cout << "GOOD: password cracked: '" << current.c_str() << "'" << std::endl;
				exit(EXIT_SUCCESS);
			}
		}
		pclose(pipe);
	}

	return EXIT_SUCCESS;
}
