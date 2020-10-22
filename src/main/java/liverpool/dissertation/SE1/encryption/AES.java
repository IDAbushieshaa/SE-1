package liverpool.dissertation.SE1.encryption;

import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public static String encrypt(String strToEncrypt, String secret, String salt) {
        try {
        	if(encryptionCipher == null)
        		encryptionCipher = initializeEncryptionCipher(secret, salt);
            return Base64.getEncoder().encodeToString(encryptionCipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    
    private static Cipher initializeEncryptionCipher(String secret, String salt) throws Exception {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return cipher;
    }
    
    private static Cipher encryptionCipher = null;
    private static Cipher decryptionCipher = null;
    
    private static Cipher initializeDecryptionCipher(String secret, String salt) throws Exception {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
    	return cipher;
    }
    
    public static String decrypt(String strToDecrypt, String secret, String salt) {
        try {
        	if(decryptionCipher == null)
        		decryptionCipher = initializeDecryptionCipher(secret, salt);
            return new String(decryptionCipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception{
    	Date date_1 = new Date();
    	String secret = "EncryptionKeyForSE1";
    	String salt = "EncryptionSaltForSE1";
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        Date date_2 = new Date();
        System.out.println(date_2.getTime() - date_1.getTime());
        
        System.out.println("===>>>");
        

        Date date1 = new Date();

        SecretKeySpec secret2 = new SecretKeySpec(secretKey.getEncoded(), "AES");
        IvParameterSpec ivSpec2 = new IvParameterSpec(ivspec.getIV());

        Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher2.init(Cipher.DECRYPT_MODE, secret2, ivSpec2);
        
        String strToDecrypt = "eyoQaPT2k0LVwkhUPBwxg5g4wFO9niGVRcMo+qQagRg=";
        
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
        
        String str = new String(decrypted);
        Date date2 = new Date();
        long l2 = date2.getTime() - date1.getTime();
        System.out.println(str + " >>> " + l2);
    }

}
