OUT=3.1 3.2 3.3 3.4 3.5 3.5.wait 3.6 shm_server shm_client

all: $(OUT) 

3.1: 3.1.c
	gcc -o 3.1 3.1.c

3.2: 3.2.c
	gcc -o 3.2 3.2.c

3.3: 3.3.c
	gcc -o 3.3 3.3.c -Wno-div-by-zero

3.4: 3.4.c
	gcc -o 3.4 3.4.c

3.5: 3.5.c
	gcc -o 3.5 3.5.c

3.5.wait: 3.5.wait.c
	gcc -o 3.5.wait 3.5.wait.c

3.6: 3.6.c
	gcc -o 3.6 3.6.c

shm_server: shm_server.c
	gcc -o shm_server shm_server.c

shm_client: shm_client.c
	gcc -o shm_client shm_client.c

clean:
	rm $(OUT)
