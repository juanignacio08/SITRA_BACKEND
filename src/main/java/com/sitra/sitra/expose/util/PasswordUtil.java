package com.sitra.sitra.expose.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class PasswordUtil {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encriptar(String password) {
        return encoder.encode(password);
    }

    public static boolean comparar(String passwordPlano, String passwordEncriptado) {
        return encoder.matches(passwordPlano, passwordEncriptado);
    }
}
