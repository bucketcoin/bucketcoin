package net.bucketcoin.wallet;

import java.security.*;
import java.util.LinkedHashSet;

public final class Wallet {

    public final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException {
        KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
        k.initialize(2048);
        KeyPair keyPair = k.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public Key getPrivateKey() {
        return privateKey;
    }

}
