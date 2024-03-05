/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package backend;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Jam
 */
public class Security {

    private byte[] key;
    private String cipherType;
    private SecretKeySpec secretKey;
    
    public Security(String key, String cipher) {
        this.key = key.getBytes();   
        this.cipherType = cipher;
        this.secretKey = new SecretKeySpec(this.key, "AES");
    }
    
    public String encrypt(String strToEncrypt) {
        String encryptedString = null;
        try {
            System.out.println("Cipher Type: " + cipherType);
            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return encryptedString;
    }

    public String decrypt(String codeDecrypt) {
        String decryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherType);     
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedString = new String(cipher.doFinal(Base64.decodeBase64(codeDecrypt)));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }      
        return decryptedString;
    }
}
