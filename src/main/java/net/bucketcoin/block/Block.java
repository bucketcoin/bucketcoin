package net.bucketcoin.block;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.bucketcoin.crypto.state.StateTrie;
import net.bucketcoin.crypto.state.StorageTrie;
import net.bucketcoin.crypto.state.TransactionTrie;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An instance of a block for the miner.
 */
public class Block implements Serializable {

    @Getter
    private final int nonce = Math.round(new SecureRandom().nextInt() * 694206942);

    @NotNull
    private final Timestamp timestamp; // the block timestamp
    private final String prevHash; // parent hash
    @Getter private double GasFee; // the gas fee to pay
    @Getter private @NotNull
    final ArrayList<Blockable> transactions; // transactions to store
    private String stateRoot;
    private String storageRoot;
    private String transactionRoot;

    public Block(@NotNull String prevHash, @NotNull ArrayList<Blockable> transactions) {
        this.prevHash = prevHash;
        this.transactions = transactions;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Block(@NotNull String prevHash, @NotNull Blockable... transactions) {
        this.prevHash = prevHash;
        this.transactions = (ArrayList<Blockable>) Arrays.asList(transactions);
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
