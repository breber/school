#define RETSIGTYPE void
#include <sys/types.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <pcap.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#ifndef setsignal_h
#define setsignal_h

RETSIGTYPE (*setsignal(int, RETSIGTYPE (*)(int)))(int);
#endif

char cpre580f98[] = "netdump";

void raw_print(u_char *user, const struct pcap_pkthdr *h, const u_char *p);
int decode_ip(const u_char *p, int *protocol);
int decode_arp(const u_char *p);
int decode_icmp(const u_char *p);
int decode_tcp(const u_char *p, int *sourcePort, int *destPort);
int decode_udp(const u_char *p);
int decode_smtp(const u_char *p);
int decode_pop(const u_char *p);
int decode_imap(const u_char *p);
int decode_http(const u_char *p);

int packettype;

char *program_name;

/* Externs */
extern void bpf_dump(struct bpf_program *, int);

extern char *copy_argv(char **);

/* Forwards */
 void program_ending(int);

/* Length of saved portion of packet. */
int snaplen = 1500;;

/* Globals to track packet counts */
int broadcastPackets = 0;
int ipPackets = 0;
int arpPackets = 0;
int icmpPackets = 0;
int tcpPackets = 0;
int dnsPackets = 0;
int smtpPackets = 0;
int popPackets = 0;
int imapPackets = 0;
int httpPackets = 0;

static pcap_t *pd;

extern int optind;
extern int opterr;
extern char *optarg;
int pflag = 0, aflag = 0;

int
main(int argc, char **argv)
{
	int cnt, op, i, done = 0;
	bpf_u_int32 localnet, netmask;
	char *cp, *cmdbuf, *device;
	struct bpf_program fcode;
	 void (*oldhandler)(int);
	u_char *pcap_userdata;
	char ebuf[PCAP_ERRBUF_SIZE];

	cnt = -1;
	device = NULL;
	
	if ((cp = strrchr(argv[0], '/')) != NULL) {
		program_name = cp + 1;
	} else {
		program_name = argv[0];
	}

	opterr = 0;
	while ((i = getopt(argc, argv, "pa")) != -1)
	{
		switch (i)
		{
		case 'p':
			pflag = 1;
		break;
		case 'a':
			aflag = 1;
		break;
		case '?':
		default:
			done = 1;
		break;
		}
		if (done) break;
	}
	if (argc > (optind)) {
	    cmdbuf = copy_argv(&argv[optind]);
	} else {
        cmdbuf = "";
    }

	if (device == NULL) {
		device = pcap_lookupdev(ebuf);
		if (device == NULL) {
			error("%s", ebuf);
		}
	}
	pd = pcap_open_live(device, snaplen,  1, 1000, ebuf);
	if (pd == NULL) {
		error("%s", ebuf);
	}
	i = pcap_snapshot(pd);
	if (snaplen < i) {
		warning("snaplen raised from %d to %d", snaplen, i);
		snaplen = i;
	}
	if (pcap_lookupnet(device, &localnet, &netmask, ebuf) < 0) {
		localnet = 0;
		netmask = 0;
		warning("%s", ebuf);
	}
	/*
	 * Let user own process after socket has been opened.
	 */
	setuid(getuid());

	if (pcap_compile(pd, &fcode, cmdbuf, 1, netmask) < 0) {
		error("%s", pcap_geterr(pd));
	}
	
	setsignal(SIGTERM, program_ending);
	setsignal(SIGINT, program_ending);
	/* Cooperate with nohup(1) */
	if ((oldhandler = setsignal(SIGHUP, program_ending)) != SIG_DFL) {
		setsignal(SIGHUP, oldhandler);
	}

	if (pcap_setfilter(pd, &fcode) < 0) {
		error("%s", pcap_geterr(pd));
	}
	pcap_userdata = 0;
    fprintf(stderr, "%s: listening on %s\n", program_name, device);
	if (pcap_loop(pd, cnt, raw_print, pcap_userdata) < 0) {
		fprintf(stderr, "%s: pcap_loop: %s\n", program_name, pcap_geterr(pd));
		exit(1);
	}
	pcap_close(pd);
	exit(0);
}

/* routine is executed on exit */
void program_ending(int signo)
{
	struct pcap_stat stat;

	if (pd != NULL && pcap_file(pd) == NULL) {
		fflush(stdout);
		putc('\n', stdout);
		if (pcap_stats(pd, &stat) < 0) {
			fprintf(stdout, "pcap_stats: %s\n", pcap_geterr(pd));
		} else {
			fprintf(stdout, "%d packets received by filter\n", stat.ps_recv);
            fprintf(stdout, "%d packets dropped by kernel\n", stat.ps_drop);

            fprintf(stdout, "Broadcast:   %d\n", broadcastPackets);
		    fprintf(stdout, "IP Packets:  %d\n", ipPackets);
		    fprintf(stdout, "ARP Packets: %d\n", arpPackets);
		    fprintf(stdout, "ICMP Packets: %d\n", icmpPackets);
		    fprintf(stdout, "TCP Packets: %d\n", tcpPackets);
		    fprintf(stdout, "DNS Packets: %d\n", dnsPackets);
		    fprintf(stdout, "SMTP Packets: %d\n", smtpPackets);
		    fprintf(stdout, "POP Packets: %d\n", popPackets);
		    fprintf(stdout, "IMAP Packets: %d\n", imapPackets);
		    fprintf(stdout, "HTTP Packets: %d\n", httpPackets);
		}
	}
	exit(0);
}

/* Like default_print() but data need not be aligned */
void
default_print_unaligned(register const u_char *cp, register u_int length)
{
	register u_int i, s;
	register int nshorts;

	nshorts = (u_int) length / sizeof(u_short);
	i = 0;
	while (--nshorts >= 0) {
		if ((i++ % 8) == 0)
			(void)printf("\n\t\t\t");
		s = *cp++;
		(void)printf(" %02x%02x", s, *cp++);
	}
	if (length & 1) {
		if ((i % 8) == 0)
			(void)printf("\n\t\t\t");
		(void)printf(" %02x", *cp);
	}
}

/*
 * By default, print the packet out in hex.
 */
void default_print(register const u_char *bp, register u_int length)
{
    register const u_short *sp;
	register u_int i;
	register int nshorts;

	if ((long)bp & 1) {
		default_print_unaligned(bp, length);
		return;
	}
	sp = (u_short *)bp;
	nshorts = (u_int) length / sizeof(u_short);
	i = 0;
	while (--nshorts >= 0) {
		if ((i++ % 8) == 0) {
			printf("\n\t");
		}
		
		printf(" %04x", ntohs(*sp++));
	}
	if (length & 1) {
		if ((i % 8) == 0) {
			printf("\n\t");
		}
		
		printf(" %02x", *(u_char *)sp);
	}
}

/*
    Print out data in custom format
*/
void raw_print(u_char *user, const struct pcap_pkthdr *h, const u_char *p)
{
    u_int length = h->len;
    u_int caplen = h->caplen;
    u_int counter;
    u_char *curLoc = (u_char *) p;
    u_char isBroadcast = 1;

    printf("DA = ");
    for (counter = 0; counter < 6; counter++) {
        printf("%02x", curLoc[counter]);

        // Is broadcast if destination address is 0xFF
        isBroadcast = isBroadcast && (curLoc[counter] == 0xFF);

        if (counter != 5) {
            printf(":");
        } else {
            printf(", ");
        }
    }
    curLoc += 6;

    // Increment the broadcast counter
    if (isBroadcast) {
        broadcastPackets++;
    }
        
    printf("SA = ");
    for (counter = 0; counter < 6; counter++) {
        printf("%02x", curLoc[counter]);

        if (counter != 5) {
            printf(":");
        } else {
            printf(", ");
        }
    }
    curLoc += 6;

    int typeLength = (curLoc[0] << 8) | curLoc[1];
    if (typeLength < 0x800) {
        printf("Len = %d", typeLength);
    } else {
        printf("Type = 0x%3x", typeLength);
    }
    
    curLoc += 2;
    
    // Print out payload information
    if (0x800 == typeLength) {
    	int protocol = 0;
    	int sourcePort = 0;
    	int destPort = 0;
        printf(", Payload = IP\n");
        ipPackets++;

		curLoc += decode_ip(curLoc, &protocol);
		
		if (protocol == 1) {
			icmpPackets++;
			curLoc += decode_icmp(curLoc);
		} else if (protocol == 6) {
			tcpPackets++;
			curLoc += decode_tcp(curLoc, &sourcePort, &destPort);
			
			if (sourcePort == 25 || destPort == 25) {
				// SMTP
				smtpPackets++;
				curLoc += decode_smtp(curLoc);
			} else if (sourcePort == 110 || destPort == 110) {
				// POP
				popPackets++;
				curLoc += decode_pop(curLoc);
			} else if (sourcePort == 143 || destPort == 143) {
				// IMAP
				imapPackets++;
				curLoc += decode_imap(curLoc);
			} else if (sourcePort == 80 || destPort == 80) {
				// HTTP
				httpPackets++;
				curLoc += decode_http(curLoc);
			}
		} else if (protocol == 17) {
			// UDP - decode to see if it is DNS
			curLoc += decode_udp(curLoc);
		}
    } else if (0x806 == typeLength) {
        printf(", Payload = ARP\n");
        arpPackets++;
        
		curLoc += decode_arp(curLoc);
    }

    putchar('\n');
}


int decode_arp(const u_char *p) {
    u_char *curLoc = (u_char *) p;
    int counter = 0;

	printf("ARP Header: \n");
	printf("\tHW Type: %d\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;
	
	printf("\tProtocol Type: 0x%x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;
	
	printf("\tHW Length: %d\n", curLoc[0]);
	curLoc += 1;

	printf("\tProtocol Length: %d\n", curLoc[0]);
	curLoc += 1;

	printf("\tOperation: %d\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	printf("\tSender HW Address = ");
	for (counter = 0; counter < 6; counter++) {
		printf("%02x", curLoc[counter]);

		if (counter != 5) {
			printf(":");
		} else {
			printf("\n");
		}
	}
	curLoc += 6;

	printf("\tSender Protocol Address = ");
	for (counter = 0; counter < 4; counter++) {
		printf("%d", curLoc[counter]);

		if (counter != 3) {
			printf(".");
		} else {
			printf("\n");
		}
	}
	curLoc += 4;

	printf("\tTarget HW Address = ");
	for (counter = 0; counter < 6; counter++) {
		printf("%02x", curLoc[counter]);

		if (counter != 5) {
			printf(":");
		} else {
			printf("\n");
		}
	}
	curLoc += 6;

	printf("\tTarget Protocol Address = ");
	for (counter = 0; counter < 4; counter++) {
		printf("%d", curLoc[counter]);

		if (counter != 3) {
			printf(".");
		} else {
			printf("\n");
		}
	}
	curLoc += 4;
	
	return curLoc - p;
}


int decode_ip(const u_char *p, int *protocol) {
    u_char *curLoc = (u_char *) p;
    int counter = 0;

	printf("IP Header: \n");
	printf("\tVersion Number: %d\n", (curLoc[0] & 0xf0) >> 4);
	printf("\tHeader Length: %d\n", (curLoc[0] & 0x0f));
	curLoc += 1;
	
	printf("\tType of Service: %d\n", curLoc[0]);
	curLoc += 1;

	printf("\tLength: %d\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	printf("\tIdentifier: 0x%04x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;
	
	printf("\tFlags: %d%d%d\n", (curLoc[0] & 0x80) >> 7, 
								(curLoc[0] & 0x40) >> 6,
								(curLoc[0] & 0x20) >> 5);
	printf("\tOffset: %d\n", (curLoc[0] & 0x1f) << 8 | curLoc[1]);
	curLoc += 2;

	printf("\tTTL: %d\n", curLoc[0]);
	curLoc += 1;
	
	printf("\tProtocol: %d\n", curLoc[0]);
	*protocol = curLoc[0];
	curLoc += 1;

	printf("\tChecksum: 0x%04x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	printf("\tSource IP Address = ");
	for (counter = 0; counter < 4; counter++) {
		printf("%d", curLoc[counter]);

		if (counter != 3) {
			printf(".");
		} else {
			printf("\n");
		}
	}
	curLoc += 4;

	printf("\tDestination IP Address = ");
	for (counter = 0; counter < 4; counter++) {
		printf("%d", curLoc[counter]);

		if (counter != 3) {
			printf(".");
		} else {
			printf("\n");
		}
	}
	curLoc += 4;
	
	return curLoc - p;
}


int decode_icmp(const u_char *p) {
    u_char *curLoc = (u_char *) p;
    int counter = 0;
    int type;
    int code;

	printf("ICMP Header: \n");
	printf("\tType: %d\n", curLoc[0]);
	type = curLoc[0];
	curLoc += 1;
	
	printf("\tCode: %d\n", curLoc[0]);
	code = curLoc[0];
	curLoc += 1;

	printf("\tChecksum: 0x%04x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;
	
	switch (type) {
		case 0:
		case 8:
		case 13:
		case 14:
			printf("\tID: %d\n", (curLoc[0] << 8) | curLoc[1]);
			curLoc += 2;
			printf("\tSeq Number: %d\n", (curLoc[0] << 8) | curLoc[1]);
			curLoc += 2;
			break;

		case 5:
			printf("\tNew Router IP Address = ");
			for (counter = 0; counter < 4; counter++) {
				printf("%d", curLoc[counter]);
		
				if (counter != 3) {
					printf(".");
				} else {
					printf("\n");
				}
			}
			curLoc += 4;
			break;

		case 3:
		case 11:
		default:
			curLoc += 4;
			break;
	}
	
	return curLoc - p;
}

int decode_tcp(const u_char *p, int *sourcePort, int *destPort) {
    u_char *curLoc = (u_char *) p;
    int counter = 0;

	printf("TCP Packet: \n");
	*sourcePort = (curLoc[0] << 8) | curLoc[1];
	printf("\tSource Port: %d\n", *sourcePort);
	curLoc += 2;

	*destPort = (curLoc[0] << 8) | curLoc[1];
	printf("\tDestination Port: %d\n", *destPort);
	curLoc += 2;

	// If either port is 53, increment DNS packet counter
	if (*sourcePort == 53 || *destPort == 53) {
		dnsPackets++;
	}

	unsigned int sequenceNumber = (curLoc[0] << 8) | curLoc[1];
	sequenceNumber = (sequenceNumber << 8) | curLoc[2];
	sequenceNumber = (sequenceNumber << 8) | curLoc[3];
	printf("\tSequence Number: %u\n", sequenceNumber);
	curLoc += 4;

	unsigned int ackNumber = (curLoc[0] << 8) | curLoc[1];
	ackNumber = (ackNumber << 8) | curLoc[2];
	ackNumber = (ackNumber << 8) | curLoc[3];
	printf("\tAcknowledgment Number: %u\n", ackNumber);
	curLoc += 4;

	int headerLength = (curLoc[0] & 0xf0) >> 4; // Number of 32 bit words
	printf("\tHeader Length: %d\n", headerLength);
	printf("\tReserved: 0x%x\n", ((curLoc[0] & 0xf) << 2) | (curLoc[1] & 0xc0) >> 6);
	curLoc += 1;
	
	printf("\tFlags: 0x%x\n", (curLoc[0] & 0x3f));
	curLoc += 1;

	printf("\tWindow Size: %d\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	printf("\tChecksum: 0x%04x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	printf("\tUrgent Pointer: 0x%04x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	return headerLength * 4;
}

int decode_udp(const u_char *p) {
    u_char *curLoc = (u_char *) p;
    int counter = 0;

	printf("UDP Packet:\n");
	
	int sourcePort = (curLoc[0] << 8) | curLoc[1];
	printf("\tSource Port: %d\n", sourcePort);
	curLoc += 2;

	int destinationPort = (curLoc[0] << 8) | curLoc[1];
	printf("\tDestination Port: %d\n", destinationPort);
	curLoc += 2;

	// If either port is 53, increment DNS packet counter
	if (sourcePort == 53 || destinationPort == 53) {
		dnsPackets++;
	}

	printf("\tUDP Total Length: %d\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	printf("\tChecksum: 0x%04x\n", (curLoc[0] << 8) | curLoc[1]);
	curLoc += 2;

	return curLoc - p;
}

int decode_smtp(const u_char *p) {
    u_char *curLoc = (u_char *) p;

	printf("SMTP Packet:\n");
	
	char temp[4048];
 	strcpy(temp, curLoc);
 	printf("\t%s", temp);
	curLoc += strlen(temp);

	return curLoc - p;
}

int decode_pop(const u_char *p) {
    u_char *curLoc = (u_char *) p;

	printf("POP Packet:\n");

	char temp[4048];
 	strcpy(temp, curLoc);
 	printf("\t%s", temp);
	curLoc += strlen(temp);

	return curLoc - p;
}

int decode_imap(const u_char *p) {
    u_char *curLoc = (u_char *) p;

	printf("IMAP Packet:\n");

	char temp[4048];
 	strcpy(temp, curLoc);
 	printf("\t%s", temp);
	curLoc += strlen(temp);

	return curLoc - p;
}


int decode_http(const u_char *p) {
    u_char *curLoc = (u_char *) p;

	printf("HTTP Packet:\n");

	char temp[4048];
 	strcpy(temp, curLoc);
 	printf("\t%s", temp);
	curLoc += strlen(temp);

	return curLoc - p;
}
