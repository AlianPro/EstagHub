package br.com.estaghub.util;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import javax.servlet.http.HttpServletRequest;

public class CryptUtil {
    public static String encryptPassword(String senha){
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.encryptPassword(senha);
    }
    public static Boolean checkPassword(String senhaAtual, String senha){
        StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
        return strongPasswordEncryptor.checkPassword(senhaAtual,senha);
    }
    public static String encryptInfo(HttpServletRequest req, String info){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ServletUtil.getContextParameter(req,"jasypt-password"));
        return textEncryptor.encrypt(info);
    }
    public static String decryptInfo(HttpServletRequest req, String infoEncrypted){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ServletUtil.getContextParameter(req,"jasypt-password"));
        return textEncryptor.decrypt(infoEncrypted);
    }
}
