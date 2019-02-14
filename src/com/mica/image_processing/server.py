#!/usr/bin/env python3

import socket
import time


HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 9000        # Port to listen on (non-privileged ports are > 1023)


            
            
def start_server():
    print('start_server begin')
    
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()
        while True:
            conn, addr = s.accept()
            with conn:
                print('Connected by', addr)
                data = conn.recv(1024)
                if not data:
                    break
                
                print("Primljena putanja: " + str(data))
                
                 
                # Wait for 2 seconds
                # simulation
                time.sleep(2)
                
                data = "0,1,1,2,0,2,1,0;2,1,2,0,0,0,1,2;0,0,0,1,0,0,0,0|3,4".encode()
                print("Poslati odgovor: " + str(data))
                conn.sendall(data)
            
    print('start_server end')
            
if __name__ == '__main__':
    start_server()