#!/usr/bin/env python3
from deep_q_network import *
import socket
import time
import re


HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 9001        # Port to listen on (non-privileged ports are > 1023)
BUFF_SIZE = 1024 # 1 Kn

            
            
def start_server():
    print('start_server_nn begin')
    
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen(5)
        
        model = build_new_model_or_load_old_model(True)
        target_model = build_new_model_or_load_old_model(True)
        
        while True:
            conn, addr = s.accept()
            with conn:
                try:
                    print('Connected by', addr)
                    data = recvall(conn)
                    #print("bytes: " + str(len(data.decode().encode('utf-8'))))
                    if not data:
                        continue
                    
                    #data = data.decode()
                    #data = data.strip() #uklanjamo whitespace-ove na pocetku i kraju
                    
                    print("Primljeni podaci: " + data)
                    tokens = data.split('|')
                    print("len(tokens) = " + str(len(tokens)))
                    if tokens[0] == "predict":
                        tokens2 = tokens[1].split(':')
                        state = convert_to_int_array(tokens2[0])
                        selektovana_polja_i_akcije = get_selektovana_polja_i_akcije(tokens2[1])
                        index_of_action, selektovano_polje = get_index_of_best_action_from_nn(model, state, selektovana_polja_i_akcije)
                        
                        print("Poslati indeks akcije: " + str(index_of_action) + ", selektovano polje: " + str(selektovano_polje))
                        ret = str(index_of_action) + ";" + str(selektovano_polje) 
                        conn.sendall(ret.encode())
                    elif tokens[0] == "train":
                        try:
                            minibatch = []
                            tokens2 = tokens[1].split(",")
                            for token2 in tokens2:
                                tokens3 = token2.split(":")
                                old_state = convert_to_int_array(tokens3[0])
                                index_of_action = int(tokens3[1])
                                nagrada = float(tokens3[2])
                                new_state = convert_to_int_array(tokens3[3])
                                minibatch.append( (old_state, index_of_action, nagrada, new_state) )
                            
                            train(minibatch, target_model, model)
                            conn.sendall("success".encode())
                        except:
                            conn.sendall("error".encode())
                    elif tokens[0] == "update":
                        try:
                            update_target_model(target_model, model)
                            conn.sendall("success".encode())
                        except:
                          conn.sendall("error".encode())
                    else:
                        raise Exception('Ovaj metod ne postoji!')
                except:
                    print("moj_error")
    print('start_server_nn end')
  
def recvall(conn):
    data = b''
    while True:
        part = conn.recv(BUFF_SIZE)
        data += part
        #if len(part) < BUFF_SIZE:
        receive_data = data.decode()
        receive_data = receive_data.strip()
        if receive_data.endswith("*"):
            break
    return receive_data[:-1] # uklanjamo zvezdicu sa kraja
  
    
def convert_to_int_array(str):
    tokens = str.split(";")
    int_array = []
    selektovana_polja = []
    
    for i in range(len(tokens)):
        #print("[" + tokens[i] + "]")
        int_array.append(int(tokens[i]))

    return int_array

def get_selektovana_polja_i_akcije(s):
    selektovana_polja_i_akcije = []
    
    tokens = s.split(',')
    
    for token in tokens:
        tokens2 = token.split('.')
        akcije = convert_to_int_array(tokens2[1])
        selektovana_polja_i_akcije.append( (int(tokens2[0], akcije)) )
        
    return selektovana_polja_i_akcije        



            
if __name__ == '__main__':
    start_server()