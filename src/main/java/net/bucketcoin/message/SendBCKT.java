package net.bucketcoin.message;

import net.bucketcoin.block.Transaction;
import net.bucketcoin.util.CryptoResources;
import net.bucketcoin.exception.InsufficientBalanceException;
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
    public void send() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InsufficientBalanceException {
        Transaction transaction = new Transaction(bckt, sender.publicKey.toString(), recipient.publicKey.toString(), gas_fee);
        String transaction_sha256 = DigestUtils.shaHex(transaction.toString());
        var c = CryptoResources.getStandardCipher();
        c.init(Cipher.ENCRYPT_MODE, sender.getPrivateKey());
        var signature = c.doFinal(transaction_sha256.getBytes(StandardCharsets.ISO_8859_1));
        if(!sender.hasEnough(bckt + (gas_fee * 0.000001))) {
            throw new InsufficientBalanceException();
        }
        Broadcast.transaction(transaction, sender.publicKey, signature); // network effect
    }
}
