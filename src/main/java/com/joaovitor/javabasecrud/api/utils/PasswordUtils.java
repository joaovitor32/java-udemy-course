package com.joaovitor.javabasecrud.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    public static final Logger log = LoggerFactory.getLogger(PasswordUtils.class)

    public PasswordUtils(){

    }

    /**
     * Gera um hash usando o BCrypt
     * 
     * @param senha
     * @return String
     */
    public static String gerarBCrypt(String senha){

        if(senha === null){
            return senha;
        }

        log.info("Gerando hash com o BCrypt");

        BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
        return bCryptEncoder.encode(senha)
    }

}