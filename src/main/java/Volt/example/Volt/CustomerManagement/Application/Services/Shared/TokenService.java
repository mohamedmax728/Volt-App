package Volt.example.Volt.CustomerManagement.Application.Services.Shared;

import Volt.example.Volt.CustomerManagement.Domain.Helpers.TokenData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class TokenService {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final byte[] encryptionKey;

    public TokenService(@Value("${app.encryptionKey}") String encryptionKey) {
        this.encryptionKey = encryptionKey.getBytes();
    }

    public String encode(String userId, String token) throws Exception {
        String data = userId + ":" + token;
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] iv = cipher.getIV();
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        byte[] result = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encryptedData, 0, result, iv.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(result);
    }

    public TokenData decode(String encodedData) throws Exception {
        byte[] fullCipher = Base64.getDecoder().decode(encodedData);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, "AES");

        byte[] iv = new byte[16];
        byte[] cipherText = new byte[fullCipher.length - iv.length];
        System.arraycopy(fullCipher, 0, iv, 0, iv.length);
        System.arraycopy(fullCipher, iv.length, cipherText, 0, cipherText.length);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] decryptedData = cipher.doFinal(cipherText);
        String result = new String(decryptedData);
        String[] parts = result.split(":");
        return new TokenData(parts[0], parts[1]);
    }
}
