package com.sicobo.sicobo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncrypter {
       public static void main(String[] args) {

        var password = "asadaASDFGFas!23";
        System.out.println("password: " + password);
        System.out.println("password encriptado:" + encriptarPassword(password));
    }

    public static String encriptarPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
