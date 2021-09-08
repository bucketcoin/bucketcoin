package net.bucketcoin.message;

import net.bucketcoin.block.Trade;
import net.bucketcoin.block.Transaction;
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
import java.util.List;

public class RequestTrade extends Message {


    private Wallet sender;
    private final Wallet recipient;
    private final List<?> toSend;
    private final List<?> toGet;
    private final double gas_fee;

    /**
     * A message to send to the recipient.
     *
     * @param sender    The sender of the message
     * @param recipient The message recipient to deliver the message to.
     * @param toSend    The {@link Object}s the request sender wishes to give.
     * @param toGet     The {@link Object}s the request sender wishes to take.
     * @param gas_fee   The gas fee (in 0.00000??? BCKT) paid to validate the message. The higher it is, the higher priority your {@code Message} gets.
     */
    public RequestTrade(@NotNull Wallet sender, @NotNull Wallet recipient, List<?> toSend, List<?> toGet, double gas_fee) {

        super(sender, recipient, toSend, toGet, gas_fee);

        this.sender = sender;
        this.recipient = recipient;
        this.toSend = toSend;
        this.toGet = toGet;
        this.gas_fee = gas_fee;
    }

    @Override
    public void send() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Trade trade = new Trade(toGet, toSend, sender.publicKey.toString(), recipient.publicKey.toString(), gas_fee);
        String trade_sha256 = DigestUtils.shaHex(trade.toString());
        var c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, sender.getPrivateKey());
        var signature = c.doFinal(trade_sha256.getBytes(StandardCharsets.US_ASCII));
        Broadcast.tradeRequest(trade, sender.publicKey, signature); // network effect
    }


}
