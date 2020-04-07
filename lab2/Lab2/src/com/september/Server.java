package com.september;

import java.math.BigInteger;

public class Server {
    private static String[] keys_tgs_ss = {"1234567890ABCDEF"};

    static boolean checkClient(){
        String message = Buffer.message;
        DESDerivedKey k_tgs_ss = new DESDerivedKey(keys_tgs_ss[0]);

        String TGS = message.split(" ")[0];
        TGS = DESCrypto.decrypt(TGS, k_tgs_ss);
        TGS = Utils.hex2text(TGS);
        System.out.println("Server got TGS: " + TGS);

        String c_ss = TGS.split(" ")[4].substring(0,16);
        DESDerivedKey k_c_ss = new DESDerivedKey(c_ss);
        String Aut2 = message.split(" ")[1];
        Aut2 = Utils.hex2text(DESCrypto.decrypt(Aut2, k_c_ss));

        System.out.println("Server got Aut2: " + Aut2);

        if (!Aut2.split(" ")[0].equals(TGS.split(" ")[0])){
            System.out.println("Authentification failed. Server don't recognize client.");
            return false;
        }

        String t4 = Aut2.split(" ")[1].substring(0,13);
        BigInteger num_t4 = new BigInteger(t4);
        num_t4 = num_t4.add(new BigInteger("1"));
        String messageForC = num_t4.toString();
        System.out.println("t4 + 1: " + messageForC);

        messageForC = DESCrypto.encrypt(Utils.text2hex(messageForC),k_c_ss);
        System.out.println("Server sends encrypted t4 + 1: " + messageForC);

        System.out.println("Server's key: " + c_ss);
        Buffer.message = messageForC;
        return true;
    }


}
