package net.bucketcoin.crypto;

import net.bucketcoin.block.Block;
import net.bucketcoin.block.Transaction;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The Bucketcoin blockchain class.
 */
public final class Bucketcoin {

    private static final Bucketcoin bucketcoin = new Bucketcoin();
    public Stack<Block> chain = new Stack<>();

    public static Bucketcoin getInstance() {
        return bucketcoin;
    }

    /**
        The chain is a singleton.
     */
    private Bucketcoin() {

        // Genesis block.
        chain.push(new Block("", new ArrayList<>(){{

            add(new Transaction(170, "spy", "yps", 3));
            add(new Transaction(692, "yps", "your mother", 9));
            add(new Transaction(694.20, "spy", "your mother", 3));

        }}));



    }

    /**
     * Get the previous block from the chain.
     */
    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    /**
     * Gets the genesis block of the chain.
     */
    public Block getGenesis() {
        return chain.get(0);
    }

    public long getChainSize() {
        return chain.size();
    }

}
