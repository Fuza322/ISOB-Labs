package com.september;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

class Server {
    private int PORT = 7777;

    private ArrayList<Integer> SYNReceivedList = new ArrayList<>();
    private ArrayList<Integer> CONNECTED = new ArrayList<>();

    private final int SYN_RECEIVED_MAXSIZE = 7;
    private final int CONNECTED_MAXSIZE = 7;


    private void addToSynReceived(TCP request, Socket clientSocket) throws Exception{
        TCP response = new TCP().with_source_port(PORT)
                .with_destination_port(request.destination_port)
                .with_syn(1);
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(response.get_String() + "\n");
        out.flush();

        SYNReceivedList.add(request.source_port);
        System.out.println("NEW SYN HAS BEEN RECEIVED.");

        System.out.print("SYN_RECEIVED: ");
        for(Integer i : SYNReceivedList)
            System.out.print(i + " ");

        System.out.print("\nCONNECTED.");
        for(Integer i : CONNECTED)
            System.out.print(i + " ");

        System.out.println("");
        if (SYNReceivedList.size() > SYN_RECEIVED_MAXSIZE) {
            System.out.println("TOO MUCH SYN...");
            throw new Exception("SYN IS OVERLOAD!");
        }
    }

    void addToConnected(TCP request) throws Exception{
        SYNReceivedList.remove(SYNReceivedList.indexOf(request.source_port));
        CONNECTED.add(request.source_port);

        System.out.println("NEW CLIENT HAS BEEN CONNECTED.");

        System.out.print("SYN_RECEIVED:");
        for(Integer i : SYNReceivedList)
            System.out.print(i + " ");

        System.out.print("\nCONNECTED:");
        for(Integer i : CONNECTED)
            System.out.print(i + " ");

        System.out.println("");
        if (CONNECTED.size() > CONNECTED_MAXSIZE) {
            System.out.println("TOO MUCH CONNECTIONS...");
            throw new Exception("SERVER IS OVERLOAD!");
        }
    }

    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    Server() throws Exception{
        try {
            try  {
                server = new ServerSocket(PORT);
                System.out.println("*********************SERVER*********************");
                System.out.println("SERVER IS WORKING...");
                System.out.println("------------------------------------------------");
                System.out.println("WAIT PLEASE...");
                System.out.println("------------------------------------------------");

                clientSocket = server.accept();
                System.out.println("SOME CLIENT IS CONNECTED");
                try {

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    while (true) {
                        String word = in.readLine();
                        System.out.println("MESSAGE: " + word.replaceAll(" ",""));
                        System.out.println("------------------------------------------------");

                        TCP request = TCP.from_string(word);
                        if (request.destination_port != PORT) {
                            continue;
                        } else if (CONNECTED.contains(request.source_port)) {
                            continue;
                        } else if (SYNReceivedList.contains(request.source_port)) {
                            this.addToConnected(request);
                            continue;
                        }
                        addToSynReceived(request, clientSocket);
                    }
                }
                catch (SocketException e){
                    System.out.println("------------------------------------------------");
                    System.out.println("CLIENT DIED!");
                }
                catch (Exception e){
                    System.out.println("------------------------------------------------");
                    System.out.println("ERROR! Something wrong...");
                    e.printStackTrace();
                } finally {
                    System.out.println("------------------------------------------------");
                    System.out.println("SOCKET CLOSING...");
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("SERVER IS DOWN");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
