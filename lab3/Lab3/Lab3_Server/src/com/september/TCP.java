package com.september;

public class TCP {

    int source_port = 0;
    int destination_port = 0;
    private int sequence_number = 0;
    private int acknowledgment_number = 0;

    private int urg = 0;
    private int ack = 0;
    private int psh = 0;
    private int rst = 0;
    private int syn = 0;
    private int fin = 0;
    private String data = "test";


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
                + " " + data;
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
                .with_data(s.split(" ")[10]);
    }
}
