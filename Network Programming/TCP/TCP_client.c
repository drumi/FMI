#include<stdio.h>
#include<unistd.h>
#include<sys/socket.h>
#include<arpa/inet.h>
#include<netinet/in.h>
#include<string.h>

#define PORT 3333
#define MAXDATASIZE 255

int my_fd, numbytes;
char buf[MAXDATASIZE];
struct sockaddr_in my_sock;

int main(int argc, char const* argv[])
{
  if (argc != 3)
  {
		fprintf(stderr,"usage: client serverIP message\n");
		return -1;
	}

  if((my_fd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
  {
    perror("socket");
    return -1;
  }

  my_sock.sin_family = AF_INET;
  my_sock.sin_addr.s_addr = inet_addr(argv[1]);
  my_sock.sin_port = htons(PORT);

  if(connect(my_fd, (struct sockaddr*)&my_sock, sizeof my_sock) == -1)
  {
    close(my_fd);
    perror("client: connect");
		return -1;
  }

  if(send(my_fd, argv[2], MAXDATASIZE, 0) == -1)
  {
    perror("send");
    return -1;
  }

  if((numbytes = recv(my_fd, buf, MAXDATASIZE - 1, 0)) == -1)
  {
    perror("recv");
		return -1;
  }

  buf[numbytes] = '\0';
	printf("client: received '%s'\n",buf);
	close(my_fd);

  return 0;
}
