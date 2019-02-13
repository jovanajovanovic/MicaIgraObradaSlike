package com.mica.main;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Klijent {
    private BufferedReader in;
    private PrintWriter out;
    
    private final int SERVER_PORT = 9000;
    private final String IP_ADDRESS = "127.0.0.1";


    public Klijent(String imagePath) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(IP_ADDRESS);

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        try {
            Socket socket = new Socket(addr, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println(imagePath);
            System.out.println("zahtev poslat");
            
            String respond = in.readLine();
            System.out.println("+++++++++++++++" + respond + "++++++++++++++");


            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


