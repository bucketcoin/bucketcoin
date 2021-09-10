package net.bucketcoin.wallet;

import net.bucketcoin.message.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.*;

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

    @Contract(" -> new")
    public @NotNull String getAddress() {
        return DigestUtils.shaHex(publicKey.toString());
    }

    public Key getPrivateKey() {
        return privateKey;
    }

}
