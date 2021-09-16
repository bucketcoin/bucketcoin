package net.bucketcoin.wallet;

import lombok.SneakyThrows;
import net.bucketcoin.message.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public final class Wallet {

    public final PublicKey publicKey;
    private final PrivateKey privateKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator k = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        k.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
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
    public void sendMessage(@NotNull Message message) {
        message.send();
    }

    public byte[] asBytes() {
        return getAddress().getBytes(StandardCharsets.US_ASCII);
    }

}
