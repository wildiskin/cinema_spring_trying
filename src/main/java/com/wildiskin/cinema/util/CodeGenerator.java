package com.wildiskin.cinema.util;


public class CodeGenerator {
    public static String generate() {
        String code = "";

        for (int i = 0; i < 6; i++) {
            code += Integer.toString((int) (Math.random() * 10));
        }

        if (code.length() != 6) {throw new RuntimeException("You an asshole!");}

        return code;
    }
}
