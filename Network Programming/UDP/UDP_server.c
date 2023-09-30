#include<sys/socket.h>
#include<arpa/inet.h>
#include<netinet/in.h>
#include<stdio.h>
#include<unistd.h>
#include<string.h>

#define SERVERPORT 3333
#define MAXBUFLEN 1000

struct sockaddr_in my_sock;
int my_fd;

char buf[MAXBUFLEN];

int numbytes;
struct sockaddr_in client_addr;
socklen_t addr_len = sizeof client_addr;
char s[INET_ADDRSTRLEN];

int main(int argc, char const *argv[])
{
	if((my_fd = socket(AF_INET, SOCK_DGRAM, 0)) == -1)
  {
		perror("socket");
		return -1;
	}

  memset(&my_sock, 0, sizeof my_sock);

  if(argc == 1)
  {
    my_sock.sin_family = AF_INET;
    my_sock.sin_addr.s_addr = INADDR_ANY;
    my_sock.sin_port = htons(SERVERPORT);
  }
  else
  {
    if (inet_pton(AF_INET, argv[1], &(my_sock.sin_addr)) != 1)
    {
			fprintf(stderr, "Invalid address\n");
			return 1;
		}

		my_sock.sin_family = AF_INET;
		my_sock.sin_port = htons(SERVERPORT);

		char ipinput[INET_ADDRSTRLEN];
		inet_ntop(AF_INET, &(my_sock.sin_addr), ipinput, INET_ADDRSTRLEN);
		printf("IP Address = %s\n", ipinput);
  }

  if(bind(my_fd, (struct sockaddr*)&my_sock, sizeof my_sock) == -1)
  {
    perror("bind");
		return -1;
  }

  for(;;)
  {
    if((numbytes = recvfrom(my_fd, buf, MAXBUFLEN-1, 0, (struct sockaddr*)&client_addr, &addr_len)) == -1)
    {
      perror("recvfrom");
			return 1;
    }

    printf("listener: got packet from %s\n",
		inet_ntop(client_addr.sin_family, &client_addr.sin_addr, s, sizeof s));
		printf("listener: packet is %d bytes long\n", numbytes);
		buf[numbytes] = '\0';
		printf("listener: packet contains \"%s\"\n", buf);
  }

  return 0;
}
