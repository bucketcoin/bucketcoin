package net.bucketcoin.block;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * An instance of a block for the miner.
 */
public class Block implements Serializable {

    @Getter
    private final int nonce = (int) Math.round(new SecureRandom().nextInt() * 694206942);

    @NotNull
    private final Timestamp timestamp;
    private final String prevHash;
    @Getter
    private double GasFee;
    @Getter
    private @NotNull ArrayList<Blockable> transactions = new ArrayList<>();

    public Block(@NotNull String prevHash, @NotNull ArrayList<Blockable> transactions) {
        this.prevHash = prevHash;
        this.transactions = transactions;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Block(@NotNull String prevHash, @NotNull Blockable transaction) {
        this.prevHash = prevHash;
        transactions.add(transaction);
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    {
        for(Blockable b : getTransactions()) GasFee += b.getGasFee();
    }

    public String getHash() {
        var a = new GsonBuilder().create();
        var b = a.toJson(this, Block.class);
        return DigestUtils.shaHex(b);
    }

}
