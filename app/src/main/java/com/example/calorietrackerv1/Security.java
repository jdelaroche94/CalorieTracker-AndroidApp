package com.example.calorietrackerv1;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * This class is responsible to encrypt and decrypt the password the DESede Algorithm
 * The class uses a secretKey which allow decrypt the key sent by the server, and compare it
 * to the password typed by the user.
 */
public class Security {

    private static String secretKey="D@123Ophjjdhnla/67T7890"; //Second Encrypted Key

    /**
     * This method is responsible to encrypt the password using DESede Algorithm and a secretKey.
     * @param password A String with the password enter by the user.
     * @return A String with the encrypted key.
     */
    public static String encryptPassword(String password){
        String base64EncryptedString="";

        try{
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[]digestOfPassword=md.digest(secretKey.getBytes("utf-8"));
            byte[]keyBytes=Arrays.copyOf(digestOfPassword,24);
            SecretKey key=new SecretKeySpec(keyBytes,"DESede");
            Cipher cipher=Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[]plainTextBytes=password.getBytes("utf-8");
            byte[]buf=cipher.doFinal(plainTextBytes);
            byte[]base64Bytes= Base64.encodeBase64(buf);
            base64EncryptedString=new String(base64Bytes);

        }catch(Exception ex){
        }
        return base64EncryptedString;
    }

    /**
     * This method is responsible to decrypt the password sent by the server, using the secretKey
     * and DESede Algorithm.
     * @param password A String with the password encrypted
     * @return A String with the password decrypted.
     * @throws Exception If the decryption fails, it returns the trace.
     */
    public static String decryptPassword(String password) throws Exception {
        String base64EncryptedString = "";
        try {
            byte[] message = Base64.decodeBase64(password.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = decipher.doFinal(message);
            base64EncryptedString = new String(plainText, "UTF-8");
        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }
}