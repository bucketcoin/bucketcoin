package net.bucketcoin.wallet;

import lombok.SneakyThrows;
import net.bucketcoin.central.CryptoResources;
import net.bucketcoin.crypto.state.StateTrie;
import net.bucketcoin.message.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.*;

public final class Wallet {

    public final PublicKey publicKey;
    private final PrivateKey privateKey;

    @SneakyThrows
    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator k = KeyPairGenerator.getInstance(CryptoResources.getStandardCipher().getAlgorithm());
        // k.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
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

    @SneakyThrows
    public void sendMessage(@NotNull Message message) {
        message.send();
    }

    public byte[] asBytes() {
        return getAddress().getBytes(StandardCharsets.ISO_8859_1);
    }

    public boolean hasEnough(double price) {
        var a = StateTrie.queryAddress(getAddress().getBytes(StandardCharsets.ISO_8859_1));
        return price <= a.balance();
    }

}
