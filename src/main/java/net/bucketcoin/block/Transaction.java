package net.bucketcoin.block;

import lombok.Getter;

import com.google.gson.GsonBuilder;
import net.bucketcoin.message.Message;
import net.bucketcoin.wallet.Wallet;

import java.io.Serializable;

public class Transaction implements Serializable, Blockable {

    @Getter
    private final double bckt;
    @Getter
    private final String sender;
    @Getter
    private final String recipient;
    @Getter
    private final double GasFee;

    /**
     * @see Message#Message(Wallet, Wallet, double, double)
     */
    public Transaction(double bckt, String sender, String recipient, double gas_fee) {
        this.bckt = bckt;
        this.sender = sender;
        this.recipient = recipient;
        this.GasFee = gas_fee;
    }


    @Override
    public String toString() {
        var g = new GsonBuilder().create();
        return g.toJson(this, Transaction.class);
    }
}
