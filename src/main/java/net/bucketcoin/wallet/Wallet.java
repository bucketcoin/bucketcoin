package net.bucketcoin.wallet;

import lombok.SneakyThrows;
import net.bucketcoin.message.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.*;

public final class Wallet {

    public final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
        k.initialize(512);
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

    @SneakyThrows
    public void sendMessage(Message message) {
        message.send();
    }

    public byte[] asBytes() {
        return getAddress().getBytes(StandardCharsets.US_ASCII);
    }

}
