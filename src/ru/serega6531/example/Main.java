package ru.serega6531.example;

import org.apache.commons.lang3.RandomStringUtils;
import ru.serega6531.oracle.PadBuster;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        String source = randomString();
        ExampleOracle oracle = new ExampleOracle(16);
        System.out.println(source);

        byte[] enc = oracle.encrypt(source);

        // without known IV (first block will not be decrypted):
        //PadBuster padBuster = new PadBuster(16, oracle);
        // with known IV:
        PadBuster padBuster = new PadBuster(16, oracle, oracle.getInitVector().getBytes(StandardCharsets.UTF_8));

        byte[] result = padBuster.attack(enc);
        String resultStr = new String(result);
        System.out.println(resultStr);

        if (source.endsWith(resultStr.substring(16))) {
            System.out.println("MATCHES");
        } else {
            System.out.println("DOES NOT MATCH");
        }
    }

    private static String randomString() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int len = rand.nextInt(50, 80);
        return RandomStringUtils.random(len, true, true);
    }

}
