#include<stdio.h>
#include<unistd.h>
#include<sys/socket.h>
#include<arpa/inet.h>
#include<netinet/in.h>
#include<string.h>

#define PORT 3333
#define MAXBUFLEN 255

struct in_addr my_addr;
struct sockaddr_in my_sock;
struct sockaddr_storage their_addr;
socklen_t sin_size = sizeof(their_addr);

int my_fd, new_fd, numbytes;
char buf[MAXBUFLEN], buf2[MAXBUFLEN];

int main(int argc, char const *argv[])
{
  if((my_fd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
  {
    perror("socket");
    return -1;
  }

  memset(&my_sock, 0, sizeof(my_sock));

  my_sock.sin_family = AF_INET;
	my_sock.sin_addr.s_addr = INADDR_ANY;
	my_sock.sin_port = htons(PORT);

  if(bind(my_fd, (struct sockaddr *)&my_sock, sizeof my_sock) == -1)
  {
    perror("bind");
    return -1;
  }

  if (listen(my_fd, 10) == -1)
  {
		perror("listen");
		return 1;
	}

  while(1)
  {
		if((new_fd = accept(my_fd, (struct sockaddr *)&their_addr, &sin_size)) == -1)
    {
			perror("accept");
			return 1;
		}

		if (!fork())
    { // this is the child process
			close(my_fd);
			if ((numbytes = recv(new_fd, buf, MAXBUFLEN-1, 0)) == -1)
      {
				perror("recv");
				return -1;
			}

      buf[MAXBUFLEN-1] = '\0';
      printf("Client sent: %s\n", buf);
      strcpy(buf2, buf);

      if (send(new_fd, buf2, 20, 0) == -1)
      {
			     perror("send");
           return -1;
      }
			close(new_fd);
			return 0;
		}
	}

  return 0;
}
