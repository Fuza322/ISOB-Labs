package com.september;

import java.util.HashMap;
import java.util.Random;

public class Utils {
    static String randomKey(){
        Random rand = new Random();
        StringBuilder key = new StringBuilder();
        for(int i = 0; i < 64; i++){
            key.append((char)('0' + rand.nextInt(2)));
        }
        return key.toString();
    }

    static String permute(String k, int[] arr, int n) {
        StringBuilder per = new StringBuilder();
        for (int i = 0; i < n; i++) {
            per.append(k.charAt(arr[i] - 1));
        }
        return per.toString();
    }

    static String hex2bin(String s) {
        HashMap<Character, String> mp = new HashMap<>();
        mp.put('0', "0000");
        mp.put('1', "0001");
        mp.put('2', "0010");
        mp.put('3', "0011");
        mp.put('4', "0100");
        mp.put('5', "0101");
        mp.put('6', "0110");
        mp.put('7', "0111");
        mp.put('8', "1000");
        mp.put('9', "1001");
        mp.put('A', "1010");
        mp.put('B', "1011");
        mp.put('C', "1100");
        mp.put('D', "1101");
        mp.put('E', "1110");
        mp.put('F', "1111");
        StringBuilder bin = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            bin.append(mp.get(s.charAt(i)));
        }
        return bin.toString();
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

    static String bin2hex(String s) {
        HashMap<String, Character> mp = new HashMap<>();
        mp.put("0000", '0');
        mp.put("0001", '1');
        mp.put("0010", '2');
        mp.put("0011", '3');
        mp.put("0100", '4');
        mp.put("0101", '5');
        mp.put("0110", '6');
        mp.put("0111", '7');
        mp.put("1000", '8');
        mp.put("1001", '9');
        mp.put("1010", 'A');
        mp.put("1011", 'B');
        mp.put("1100", 'C');
        mp.put("1101", 'D');
        mp.put("1110", 'E');
        mp.put("1111", 'F');

        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < s.length(); i += 4) {
            hex.append(mp.get(s.substring(i,i + 4)));
        }
        return hex.toString();
    }

    static String text2hex(String message){
        return bin2hex(text2bin(message));
    }

    static String hex2text(String message){
        return bin2text(hex2bin(message));
    }
}
