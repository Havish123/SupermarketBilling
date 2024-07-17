package Extension;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityConfig {
    private static String EncryptionKey="qMBM3lqtUW5Lu93K2jH2v6npGMjw8C37";

    public static String getEncryptionKey(){
        return EncryptionKey;
    }

    public static String EncryptData(String input){
        try{
            byte[] decodedKey = Base64.getDecoder().decode(EncryptionKey);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(encryptedBytes);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return input;
    }

    public static String DecryptData(String input){
        try{
            byte[] decodedKey = Base64.getDecoder().decode(EncryptionKey);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));

            return new String(decryptedBytes, "UTF-8");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return input;
    }

    public static  Boolean validatePassword(String password){
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,12}$";

        Pattern pattern = Pattern.compile(passwordPattern);

        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static Boolean validateEmailId(String email){
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailPattern);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static Boolean validateMobileNumber(String mobileNumber){
        String mobileNumberPattern = "^[0-9]{10}$";

        Pattern pattern = Pattern.compile(mobileNumberPattern);

        Matcher matcher = pattern.matcher(mobileNumber);

        return matcher.matches();
    }


}
