package com.september;

import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.sql.Timestamp;
import java.util.Arrays;

import static com.september.DESCrypto.encrypt;

public class KerberosServer {
    private static String[] keys_as_tgs = {"FEDCBA0987654321"};
    private static String[] keys_c_tgs = {"1122334455667788"};

    public static class AS{
        static String[] clients = {"Client_C"};
        static String[] keys_c = {"1234567890ABCDEF"};

        //static String[] keys_as_tgs = {"FEDCBA0987654321"};
        //static String[] keys_c_tgs = {"1122334455667788"};

        static boolean giveTGT(){
            String id = Buffer.message;
            System.out.println("Authentication Server(AS) checked that this client exist in DB.");

            if (!Arrays.asList(clients).contains(id)){
                System.out.println("ERROR. CLIENT DOES NOT EXIST!");
                return false;
            }
            System.out.println("OK.");
            System.out.println("-------------------------------------------------------------------------");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String TGT = id + " " + TGS.id + " " + timestamp.getTime() + " " + "10000000";
            System.out.println("Authentication Server(AS) creates TGT: " + TGT);

            DESDerivedKey as_tgs = new DESDerivedKey(keys_as_tgs[0]);
            TGT = Utils.text2hex(TGT);
            String TGT_encrypted = DESCrypto.encrypt(TGT, as_tgs);
            System.out.println("Encrypted TGT: " + TGT_encrypted);


            DESDerivedKey c = new DESDerivedKey(keys_c[0]);
            String messageForC = TGT_encrypted + " " + keys_c_tgs[0];
            System.out.println("TGT + key for C: " + messageForC);
            messageForC = DESCrypto.encrypt(Utils.text2hex(messageForC), c);

            System.out.println("Encrypted TGT + key for C: " + messageForC);
            Buffer.message = messageForC;
            return true;
        }
    }

    public static class TGS{
        static String id = "tgs1";
        private static String[] keys_tgs_ss = {"1234567890ABCDEF"};


        static boolean giveTGT(){
            System.out.println(Buffer.message);
            String ID = Buffer.message.split(" ")[2];
            String Aut = Buffer.message.split(" ")[1];
            String TGT = Buffer.message.split(" ")[0];

            DESDerivedKey k_as_tgs = new DESDerivedKey(keys_as_tgs[0]);
            TGT = Utils.hex2text(DESCrypto.decrypt(TGT, k_as_tgs));
            System.out.println("Encrypted TGT: " + TGT);

            DESDerivedKey k_c_tgs = new DESDerivedKey(keys_c_tgs[0]);
            Aut = Utils.hex2text(DESCrypto.decrypt(Aut, k_c_tgs));
            System.out.println("Encrypted Aut: " + Aut);

            if (!Aut.split(" ")[0].equals(TGT.split(" ")[0])){
                System.out.println("Authentication error.");
                return false;
            }

            if (!TGT.split(" ")[1].equals(id)){
                System.out.println("Wrong TGS server.");
                return false;
            }

            BigInteger n1 = new BigInteger(Aut.split(" ")[1].substring(0,13));
            BigInteger n2 = new BigInteger(TGT.split(" ")[2]);
            BigInteger n3 = new BigInteger(TGT.split(" ")[3].substring(0,7));
            n1 = n1.subtract(n2);
            if (n1.compareTo(n3) > 0){
            //if (Integer.parseInt(Aut.split(" ")[1].substring(0,13)) - Integer.parseInt(TGT.split("")[2]) > Integer.parseInt(TGT.split(" ")[3].substring(0,7)) ){
                System.out.println("Time of ticket expired!");
                return false;
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String c_ss = Utils.bin2hex(Utils.randomKey());
            System.out.println("TGS server generate password C_SS: " + c_ss);
            String TGS = TGT.split(" ")[0] + " " + ID + " " + timestamp.getTime() + " 1000000 " + c_ss;
            System.out.println("TGS creates TGS ticket: " + TGS);

            TGS = Utils.text2hex(TGS);
            DESDerivedKey k_tgs_ss = new DESDerivedKey(keys_tgs_ss[0]);
            TGS = DESCrypto.encrypt(TGS, k_tgs_ss);
            System.out.println("Encrypted TGS: " + TGS);

            TGS = Utils.text2hex(TGS + " " + c_ss);
            TGS = DESCrypto.encrypt(TGS, k_c_tgs);
            System.out.println("Encrypted TGS + K_c_ss: " + TGS);

            Buffer.message = TGS;
            return true;
        }
    }

}
