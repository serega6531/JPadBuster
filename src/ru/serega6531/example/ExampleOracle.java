package ru.serega6531.example;

import org.apache.commons.lang3.RandomStringUtils;
import ru.serega6531.oracle.Oracle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class ExampleOracle implements Oracle {

    private String initVector;
    private String key;

    public ExampleOracle(int blockLength) {
        this.initVector = randomString(blockLength);
        this.key = randomString(16);

        System.out.println("iv = " + initVector);
        System.out.println("key = " + key);
    }

    public String getInitVector() {
        return initVector;
    }

    public byte[] encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            return cipher.doFinal(value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean tryDecrypt(byte[] encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            Thread.sleep(5);   // imitate network delay

            cipher.doFinal(encrypted);
            return true;
        } catch (BadPaddingException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String randomString(int len) {
        return RandomStringUtils.random(len, true, true);
    }

}
