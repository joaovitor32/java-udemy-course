package com.joaovitor.javabasecrud.api.utils;

import static org.junit.Assert.assertNull;

public class PasswordUtilsTest {

    private static final String SENHA = "123456";
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

    @Test
    public void testSenhaNula() throws Exception {
        assertNull(PasswordUtils.gerarBycrypt(null));
    }

    @Test 
    public void testGerarHashSenha() throws Exception {
        String hash = PasswordUtils.gerarBycrypt(SENHA);

        assertTrue(bCryptEncoder.matches(SENHA, hash));
    }

}