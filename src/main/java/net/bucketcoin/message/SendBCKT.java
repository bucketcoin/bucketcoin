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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SendBCKT extends Message {

    public SendBCKT(@NotNull Wallet sender, @NotNull Wallet recipient, int bckt, int gas_fee) throws

            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {

        super(sender, recipient, gas_fee);
        Transaction transaction = new Transaction(bckt, sender.publicKey.toString(), recipient.publicKey.toString(), gas_fee);
        String transaction_sha256 = DigestUtils.shaHex(transaction.toString());
        var c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, sender.getPrivateKey());
        var signature = c.doFinal(transaction_sha256.getBytes());
        Broadcast.transaction(transaction, sender.publicKey, signature); // network effect
    }

}
