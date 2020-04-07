package com.september;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Client {
    String id = "Client_C";
    String key = "1234567890ABCDEF";
    String ss_id = "Server1";

    void startProtocol(){
        System.out.println("Client(C) sends his ID to Authentication Server(AS): " + id);
        Buffer.message = id;
        if (!KerberosServer.AS.giveTGT()){
            return;
        }

        DESDerivedKey c_key = new DESDerivedKey(key);

        String TGT_encrypted = Buffer.message;
        String TGT = DESCrypto.decrypt(TGT_encrypted, c_key);
        TGT = Utils.hex2text(TGT);

        System.out.println("Client decrypts crypted TGT: " + TGT);

        String c_tgs = TGT.split(" ")[1];
        c_tgs = c_tgs.substring(0,16);
        TGT = TGT.split(" ")[0];

        System.out.println("Client splits key (" + c_tgs + ") and encrypted TGT: " + TGT);


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String Aut = id + " " + timestamp.getTime();

        BigInteger t = new BigInteger(Aut.split(" ")[1]);

        System.out.println("Client creates Aut: " + Aut);
        DESDerivedKey k_c_tgs = new DESDerivedKey(c_tgs);
        Aut = DESCrypto.encrypt(Utils.text2hex(Aut), k_c_tgs);
        System.out.println("and encrypt it:" + Aut);

        Buffer.message = TGT + " " + Aut + " " + ss_id;

        KerberosServer.TGS.giveTGT();
        String message = Buffer.message;
        message = Utils.hex2text(DESCrypto.decrypt(message, k_c_tgs));
        System.out.println("Client encrypts message: " + message);

        String c_ss = message.split(" ")[1];
        DESDerivedKey k_c_ss = new DESDerivedKey(c_ss);

        String TGS = message.split(" ")[0];

        String Aut2 = id + " " + timestamp.getTime();
        System.out.println("Client Aut2: " + Aut2);
        Aut = DESCrypto.encrypt(Utils.text2hex(Aut2), k_c_ss);
        message = TGS + " " + Aut;

        System.out.println("Message for SS: " + message);
        Buffer.message = message;
        Server.checkClient();

        String time = Buffer.message;
        time = Utils.hex2text(DESCrypto.decrypt(time,k_c_ss));

        time = time.substring(0,13);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Client decrypt t4 + 1:" + time + "(Client's t4: " + t.toString() + ")");
        t = t.add(new BigInteger("1"));

        BigInteger t2 = new BigInteger(time);
        if (t.compareTo(t2) != 0){
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("Client t4 + 1 doesn't match server's. Connection failed.");
            return;
        }
        System.out.println("Client's key: " + c_ss);
        System.out.println("-------------------------------------------------------------------------");

        System.out.println("Success!");
    }

}
