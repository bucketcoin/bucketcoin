package net.bucketcoin.message;

import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequestTrade extends Message {


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

    }
}
