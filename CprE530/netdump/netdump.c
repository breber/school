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
		putc('\n', stderr);
		if (pcap_stats(pd, &stat) < 0) {
			fprintf(stderr, "pcap_stats: %s\n", pcap_geterr(pd));
		} else {
			fprintf(stderr, "%d packets received by filter\n", stat.ps_recv);
            fprintf(stderr, "%d packets dropped by kernel\n", stat.ps_drop);

            fprintf(stderr, "Broadcast:   %d\n", broadcastPackets);
		    fprintf(stderr, "IP Packets:  %d\n", ipPackets);
		    fprintf(stderr, "ARP Packets: %d\n", arpPackets);
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
        printf("Len  = %5d", typeLength);
    } else {
        printf("Type = 0x%3x", typeLength);
    }
    
    // Print out payload information
    if (0x800 == typeLength) {
        printf(", Payload = IP");
        ipPackets++;
    } else if (0x806 == typeLength) {
        printf(", Payload = ARP");
        arpPackets++;
    }

    //printf("\n");
    //default_print(p, caplen);
    putchar('\n');
}
