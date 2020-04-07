package com.september;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Hacker2 {
    Hacker2(){
        Socket clientSocket;
        BufferedReader reader;
        BufferedReader in;
        BufferedWriter out;
        try {
            clientSocket = new Socket("localhost", 7777); // этой строкой мы запрашиваем
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            for(int i = 0; i < 10; i++ ) {
                String request = new TCP().with_source_port(1488 + i).with_destination_port(7777).with_syn(1).get_String();
                out.write(request + "\n");
                out.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
