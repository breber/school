/*
 *******************************************************************************
 *
 *  tcp_client.c --
 *	program to send packets to a server (default port 2000)
 *
 *******************************************************************************
 */

#include <sys/types.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <string.h>
#include <arpa/inet.h>

/*
 *  Initialize the socket address info struct.
 */

struct sockaddr_in sock_in, temp, from_addr;
int	from_len;

extern	int	errno;

int main(int argc, char **argv)
{
	struct timeval	timeout;
	register int	n;
	u_short 		len;
	char 			*cp;
	int 			i, retry, resplen, done = 0;
	int				port = 2000;
	int 			dsmask, flags, sockFD;
	char			buf[100], answer[4048];
	char			hostname[100];
	struct hostent	*h_name;
	struct servent	*s_name;
	int				numTimeOuts	= 0;
	FILE 			*file = NULL;

	sockFD = -1;

	// Change this to match the hostname of the tcp_server
	strcpy(hostname, "spock.ee.iastate.edu");

	opterr = 0;
	while ((i = getopt(argc, argv, "hpft")) != -1)
	{
		switch (i)
		{
		case 'h':
			strcpy(hostname, argv[optind]);
			optind++;
		break;
		case 't':
			// this is a test flag to show how the flags work
			// this will print out the parms
			printf("%s\n", argv[optind]);
			optind++;
		break;
		case 'p':
			// add code for the p flag set
			port = atoi(argv[optind]);
			optind++;
		break;
		case 'f':
			// add code for the f flag set
			file = fopen(argv[optind], "r");
			optind++;
		break;
		case '?':
		default:
			done = 1;
		break;
		}
		if (done) break;
	}

	h_name = gethostbyname(hostname);
	sock_in.sin_family = AF_INET;
	sock_in.sin_port = htons(port);
	sock_in.sin_addr.s_addr	= *(u_long *)h_name->h_addr;
	printf("port = %d -- %s (%s)\n", ntohs(sock_in.sin_port), hostname, inet_ntoa(sock_in.sin_addr));
	// Send request
	sockFD = socket(AF_INET, SOCK_STREAM, 0);

	if (connect(sockFD, (struct sockaddr *) &sock_in, sizeof(sock_in)) < 0) {
		perror("connect request");
		(void) close(sockFD);
		exit(1);
	}
	
	if (!file) {
		strcpy(buf,"from client");
		if (send(sockFD, buf, strlen(buf), 0) != strlen(buf)) {
			perror("send request");
			(void) close(sockFD);
			exit(1);
		}
	} else {
		while (fgets(buf, 100, file)) {
			if (send(sockFD, buf, strlen(buf), 0) != strlen(buf)) {
				perror("send request");
				(void) close(sockFD);
				exit(1);
			}
		}
	}
	cp = answer;
	if ((n = recv(sockFD, cp, 100, 0)) < 0){
		perror("SendRequest");
		(void) close(sockFD);
	}
	cp[n] = 0;
	printf("===<%s>===\n",cp);
	(void) close(sockFD);
	exit(1);
}
