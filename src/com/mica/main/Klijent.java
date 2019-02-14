package com.mica.main;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Klijent {
    private static final int SERVER_PORT = 9000;
    private static final String IP_ADDRESS = "127.0.0.1";

    private  Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    public String posaljiPutanjuDoSlikeISacekajOdgovor(String putanjaDoSlike) throws Exception {
    	InetAddress addr = InetAddress.getByName(IP_ADDRESS);
     
    	socket = new Socket(addr, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        out.println(putanjaDoSlike);
        System.out.println("zahtev poslat");
        
        String respond = in.readLine();
        System.out.println("+++++++++++++++" + respond + "++++++++++++++");


        in.close();
        out.close();
        socket.close();
        
        return respond;

    }
    
    public void stop() throws Exception {
    	in.close();
        out.close();
        socket.close();
    }

}


