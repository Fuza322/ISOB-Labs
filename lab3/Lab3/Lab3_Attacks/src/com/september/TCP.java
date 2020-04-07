package com.september;

public class TCP {
    int source_port = 0;
    int destination_port = 0;
    int sequence_number = 0;
    int acknowledgment_number = 0;
    int urg = 0;
    int ack = 0;
    int psh = 0;
    int rst = 0;
    int syn = 0;
    int fin = 0;
    String data = "test";

    TCP with_source_port(int val){
        source_port = val;
        return this;
    }
    TCP with_destination_port(int val){
        destination_port = val;
        return this;
    }
    TCP with_sequence_number(int val){
        sequence_number = val;
        return this;
    }
    TCP with_acknowledgment_number(int val){
        acknowledgment_number = val;
        return this;
    }
    TCP with_urg(int val){
        urg = val;
        return this;
    }
    TCP with_ack(int val){
        ack = val;
        return this;
    }
    TCP with_psh(int val){
        psh = val;
        return this;
    }
    TCP with_rst(int val){
        rst = val;
        return this;
    }
    TCP with_syn(int val){
        syn = val;
        return this;
    }
    TCP with_fin(int val){
        fin = val;
        return this;
    }
    TCP with_data(String val){
        data = val;
        return this;
    }

    String get_String(){
        String array = "" + source_port     //{0:016b}
                + " " + destination_port              //{0:016b}
                + " " +sequence_number  //{0:032b}
                + " " + acknowledgment_number //{0:032b}
                + " " + urg
                + " " + ack
                + " " + psh
                + " " + rst
                + " " + syn
                + " " + fin
                + " " + text2bin(data);
        return array;
    }

    static TCP from_string(String s){
        TCP obj = new TCP();
        return obj.with_source_port(Integer.parseInt(s.split(" ")[0],10))
                .with_destination_port(Integer.parseInt(s.split(" ")[1],10))
                .with_sequence_number(Integer.parseInt(s.split(" ")[2],10))
                .with_acknowledgment_number(Integer.parseInt(s.split(" ")[3],10))
                .with_urg((s.split(" ")[4].charAt(0) - '0'))
                .with_ack((s.split(" ")[5].charAt(0) - '0'))
                .with_psh((s.split(" ")[6].charAt(0) - '0'))
                .with_rst((s.split(" ")[7].charAt(0) - '0'))
                .with_syn((s.split(" ")[8].charAt(0) - '0'))
                .with_fin((s.split(" ")[9].charAt(0) - '0'))
                .with_data(bin2text(s.split(" ")[10]));
    }

    static String text2bin(String s){
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for(byte b : bytes){
            int val = b;
            for(int i = 0; i < 8; i++){
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    static String bin2text(String s){
        StringBuilder text = new StringBuilder();
        for(int i = 0; i <= s.length() - 8; i += 8){
            text.append((char)Integer.parseInt(s.substring(i,i+8),2));
        }
        return text.toString();
    }

}
