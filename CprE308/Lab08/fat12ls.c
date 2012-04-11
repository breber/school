/* 
* fat12ls.c 
* 
* Displays the files in the root sector of an MSDOS floppy disk
*
* Brian Reber
* CprE 308, Section D
*/
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <fcntl.h>
#include <string.h>

#define SIZE 32      /* size of the read buffer */
#define ROOTSIZE 256 /* max size of the root directory */
//define PRINT_HEX   // un-comment this to print the values in hex for debugging

struct BootSector
{
	unsigned char  sName[9];            // The name of the volume
	unsigned short iBytesSector;        // Bytes per Sector

	unsigned char  iSectorsCluster;     // Sectors per Cluster
	unsigned short iReservedSectors;    // Reserved sectors
	unsigned char  iNumberFATs;         // Number of FATs

	unsigned short iRootEntries;        // Number of Root Directory entries
	unsigned short iLogicalSectors;     // Number of logical sectors
	unsigned char  xMediumDescriptor;   // Medium descriptor

	unsigned short iSectorsFAT;         // Sectors per FAT
	unsigned short iSectorsTrack;       // Sectors per Track
	unsigned short iHeads;              // Number of heads

	unsigned short iHiddenSectors;      // Number of hidden sectors
};

void parseDirectory(int iDirOff, int iEntries, unsigned char buffer[]);
//  Pre: Calculated directory offset and number of directory entries
// Post: Prints the filename, time, date, attributes and size of each entry

unsigned short endianSwap(unsigned char one, unsigned char two);
//  Pre: Two initialized characters
// Post: The characters are swapped (two, one) and returned as a short

void decodeBootSector(struct BootSector * pBootS, unsigned char buffer[]);
//  Pre: An initialized BootSector struct and a pointer to an array
//       of characters read from a BootSector
// Post: The BootSector struct is filled out from the buffer data

char * toDOSName(char string[], unsigned char buffer[], int offset);
//  Pre: String is initialized with at least 12 characters, buffer contains
//       the directory array, offset points to the location of the filename
// Post: fills and returns a string containing the filename in 8.3 format

char * parseAttributes(char string[], unsigned char key);
//  Pre: String is initialized with at least five characters, key contains
//       the byte containing the attribue from the directory buffer
// Post: fills the string with the attributes

char * parseTime(char string[], unsigned short usTime);
//  Pre: string is initialzied for at least 9 characters, usTime contains
//       the 16 bits used to store time
// Post: string contains the formatted time

char * parseDate(char string[], unsigned short usDate);
//  Pre: string is initialized for at least 13 characters, usDate contains
//       the 16 bits used to store the date
// Post: string contains the formatted date

int roundup512(int number);
// Pre: initialized integer
// Post: number rounded up to next increment of 512


// reads the boot sector and root directory
int main(int argc, char * argv[]) {
	int pBootSector = 0;
	unsigned char buffer[SIZE];
	unsigned char rootBuffer[ROOTSIZE * 32];
	struct BootSector sector;
	int iRDOffset = 0;

	// Check for argument
	if (argc < 2) {
		printf("Specify boot sector\n");
		return 0;    	
	}

	// Open the file and read the boot sector
	pBootSector = open(argv[1], O_RDONLY);
	read(pBootSector, buffer, SIZE);

	// Decode the boot Sector
	decodeBootSector(&sector, buffer);

	// Calculate the location of the root directory
	iRDOffset = (1 + (sector.iSectorsFAT * sector.iNumberFATs)) * sector.iBytesSector;

	// Read the root directory into buffer
	lseek(pBootSector, iRDOffset, SEEK_SET);
	read(pBootSector, rootBuffer, ROOTSIZE);
	close(pBootSector);

	// Parse the root directory
	parseDirectory(iRDOffset, sector.iRootEntries, rootBuffer);
} // end main


// Converts two characters to an unsigned short with two, one
unsigned short endianSwap(unsigned char one, unsigned char two) {
	return (two << 8) | one;
}


// Fills out the BootSector Struct from the buffer
void decodeBootSector(struct BootSector * pBootS, unsigned char buffer[]) {
	int i = 3;  // Skip the first 3 bytes
	int tmp;

	// Pull the name and put it in the struct (remember to null-terminate)
	for (tmp = 0; tmp < 8; tmp++, i++) {
		pBootS->sName[tmp] = buffer[i];
	}
	pBootS->sName[tmp] = '\0';

	// Read bytes/sector and convert to big endian
	pBootS->iBytesSector = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;
 
	// Read sectors/cluster, Reserved sectors and Number of Fats
	pBootS->iSectorsCluster = buffer[i++];

	pBootS->iReservedSectors = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;

	pBootS->iNumberFATs = buffer[i++];

	// Read root entries, logicical sectors and medium descriptor
	pBootS->iRootEntries = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;

	pBootS->iLogicalSectors = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;

	pBootS->xMediumDescriptor = buffer[i++];

	// Read and covert sectors/fat, sectors/track, and number of heads
	pBootS->iSectorsFAT = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;

	pBootS->iSectorsTrack = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;

	pBootS->iHeads = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;
    
	// Read hidden sectors
	pBootS->iHiddenSectors = endianSwap(buffer[i], buffer[i + 1]);
	i += 2;
    
	return;
}


// iterates through the directory to display filename, time, date,
// attributes and size of each directory entry to the console
void parseDirectory(int iDirOff, int iEntries, unsigned char buffer[]) {
	int i = 0;
	char string[13];

	// Display table header with labels
	printf("Filename\tAttrib\tTime\t\tDate\t\tSize\n");

	// loop through directory entries to print information for each
	for (i = 0; i < (iEntries); i = i + 32) {
		if (buffer[i] != 0x00 && buffer[i] != 0xE5) {
			printf("%d\t", iDirOff + i);
			// Display filename
			printf("%s\t", toDOSName(string, buffer, i));
			// Display Attributes
			printf("%s\t", parseAttributes(string, buffer[i + 0x0b]));
			// Display Time
			printf("%s\t", parseTime(string, endianSwap(buffer[i + 0x16], buffer[i + 0x17])));
			// Display Date
			printf("%s\t", parseDate(string, endianSwap(buffer[i + 0x18], buffer[i + 0x19])));
			// Display Size
			int size = buffer[i + 0x1c] | (buffer[i + 0x1d] << 8) | (buffer[i + 0x1e] << 16)
						| (buffer[i + 0x1f] << 24);
			printf("%d\n", size);
		}
	}

	// Display key
	printf("(R)ead Only (H)idden (S)ystem (A)rchive\n");
} // end parseDirectory()


// Parses the attributes bits of a file
char * parseAttributes(char string[], unsigned char key) {
	// Clear the string
	memset(string, 0, 13);

	// Check the attributes
	int i = 0;
	if (key & 0x01) {
		string[i++] = 'R';
	}
	if (key & 0x02) {
		string[i++] = 'H';
	}
	if (key & 0x04) {
		string[i++] = 'S';
	}
	if (key & 0x20) {
		string[i++] = 'A';
	}

	return string;
} // end parseAttributes()


// Decodes the bits assigned to the time of each file
char * parseTime(char string[], unsigned short usTime) {
	unsigned char hour = 0x00, min = 0x00, sec = 0x00;

	sec = (usTime & 0x1F) << 1; // multiply times two
	min = (usTime >> 5) & 0x3F;
	hour = (usTime >> 11) & 0x1F;

	sprintf(string, "%02i:%02i:%02i", hour, min, sec);

	return string;
} // end parseTime()


// Decodes the bits assigned to the date of each file
char * parseDate(char string[], unsigned short usDate) {
	unsigned char month = 0x00, day = 0x00;
	unsigned short year = 0x0000;

	day = usDate & 0x1F;
	month = (usDate >> 5) & 0xF;
	year = 1980 + ((usDate >> 9) & 0x7F);

	sprintf(string, "%d/%d/%d", year, month, day);

	return string;
} // end parseDate()


// Formats a filename string as DOS (adds the dot to 8-dot-3)
char * toDOSName(char string[], unsigned char buffer[], int offset) {
	// Clear the string
	memset(string, 0, 13);

	// This is stub code!
	int i;
	for (i = offset; i < offset + 8; i++) {
		string[i - offset] = buffer[i];
	}

	string[i - offset] = '.';

	for (; i < offset + 11; i++) {
		string[i + 1 - offset] = buffer[i];
	}

	return string;
} // end toDosNameRead-Only Bit
