package com.september;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;


import static java.util.Collections.reverse;

public class Main {

    public static void main(String[] args) {
        //---------------------------------------------------------------------------------------------
        // DES
        String pt = "1000000000000000";
        String key = "1234567890ABCDEF";

        DESCrypto.DEBUG = true;

        DESDerivedKey dKey = new DESDerivedKey(key);

        System.out.println("\n********************************* DES ***********************************");
        System.out.println("                         !!! STEP ENCRYPTION !!!");
        System.out.print("-------------------------------------------------------------------------");
        String cipher = DESCrypto.encrypt(pt, dKey);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("CIPHER TEXT: " + cipher );
        System.out.println("-------------------------------------------------------------------------\n\n");
        System.out.println("*************************************************************************");
        System.out.println("                         !!! STEP DECRYPTION !!!");
        System.out.print("-------------------------------------------------------------------------");
        dKey.rev();
        String text = DESCrypto.encrypt(cipher, dKey);
        System.out.print("-------------------------------------------------------------------------");
        System.out.println("\nPLAIN TEXT: " + text);
        System.out.println("-------------------------------------------------------------------------\n");

        //---------------------------------------------------------------------------------------------
        // Kerberos-Server
        System.out.println("********************************* KERBEROS ***********************************");
        System.out.println("Starting...");
        DESCrypto.DEBUG = false;
        System.out.println("-------------------------------------------------------------------------");
        Client client_C = new Client();
        client_C.startProtocol();
        System.out.println("-------------------------------------------------------------------------");

    }
}
