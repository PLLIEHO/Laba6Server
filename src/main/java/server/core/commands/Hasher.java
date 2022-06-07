package server.core.commands;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] message = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger hashed = new BigInteger(1, message);
            return hashed.toString(16);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
