package net.bucketcoin.message;

import net.bucketcoin.block.Transaction;
import net.bucketcoin.node.Miner;
import net.bucketcoin.p2p.Broadcast;
import net.bucketcoin.wallet.Wallet;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SendBCKT extends Message {

    private final Wallet sender;
    private final Wallet recipient;
    private final double bckt;
    private final double gas_fee;

    public SendBCKT(@NotNull Wallet sender, @NotNull Wallet recipient, double gas_fee, double bckt) {

        super(sender, recipient, gas_fee, bckt);
        this.sender = sender;
        this.recipient = recipient;
        this.bckt = bckt;
        this.gas_fee = gas_fee;
    }

    @Override
    public void send() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Transaction transaction = new Transaction(bckt, sender.publicKey.toString(), recipient.publicKey.toString(), gas_fee);
        String transaction_sha256 = DigestUtils.shaHex(transaction.toString());
        var c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, sender.getPrivateKey());
        var signature = c.doFinal(transaction_sha256.getBytes(StandardCharsets.US_ASCII));
        Broadcast.transaction(transaction, sender.publicKey, signature); // network effect
    }
}
