package com.september;

import java.util.ArrayList;

import static com.september.Utils.*;
import static java.util.Collections.reverse;

class DESDerivedKey {
    ArrayList<String> rkb;

    DESDerivedKey(DESDerivedKey toCopy){
        this.rkb = new ArrayList<>(toCopy.rkb);
    }

    DESDerivedKey(String key){
        key = hex2bin(key);
        int[] keyp = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };
        key = permute(key, keyp, 56); // key without parity
        int[] shift_table = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
        int[] key_comp = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

        String left = key.substring(0, 28);
        String right = key.substring(28);

        ArrayList<String> rkb = new ArrayList<>(); // rkb for RoundKeys in binary
        for (int i = 0; i < 16; i++) {
            left = shift_left(left, shift_table[i]);
            right = shift_left(right, shift_table[i]);
            String combine = left.concat(right);
            // Key Compression
            String RoundKey = permute(combine, key_comp, 48);
            rkb.add(RoundKey);
        }
        this.rkb = rkb;
    }

    private String shift_left(String kS, int shifts) {
        StringBuilder k = new StringBuilder(kS);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < shifts; i++) {
            for (int j = 1; j < 28; j++) {
                s.append(k.charAt(j));
            }
            s.append(k.charAt(0));
            k = new StringBuilder(s);
            s.delete(0,s.length());
        }
        return k.toString();
    }

    void rev(){
        reverse(this.rkb);
    }
}
