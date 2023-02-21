package br.com.estaghub.util;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class CryptUtil {
    public static String encryptPassword(String senha){
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.encryptPassword(senha);
    }
    public static Boolean checkPassword(String senhaAtual, String senha){
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.checkPassword(senhaAtual,senha);
    }
}
