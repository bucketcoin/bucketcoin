package net.bucketcoin.crypto;

import net.bucketcoin.block.Block;
import net.bucketcoin.block.Transaction;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static net.bucketcoin.algorithm.DifficultyAlgorithm.calculateDifficulty;

/**
 * The Bucketcoin blockchain class.
 */
public final class Bucketcoin {

    public static final int BSCIVersion = 1;

    private static final Bucketcoin bucketcoin = new Bucketcoin();
    public Stack<Block> chain = new Stack<>();

    public static Bucketcoin getInstance() {
        return bucketcoin;
    }

    /**
        The chain is a singleton.
     */
    private Bucketcoin() throws SecurityException {

        AtomicBoolean threadInterrupted = new AtomicBoolean(false);
        AtomicLong durationUntil1500thBlock = new AtomicLong(0);

        // Genesis block.
        chain.push(new Block("", new ArrayList<>(){{

            add(new Transaction(170, "spy", "yps", 3));
            add(new Transaction(692, "yps", "your mother", 9));
            add(new Transaction(694.20, "spy", "your mother", 3));

        }}));

        var t1 = new Thread(null, () -> {
            int x = chain.size();
            boolean k = false;
            while(!threadInterrupted.get()) {
                if(x % 1500 == 0 && !k && x != 0) {
                    calculateDifficulty(1500, Duration.ofSeconds(durationUntil1500thBlock.getPlain()));
                    k = true;
                    ++x;
                } else if(x % 1500 == 0 && k && x != 0
                || k && x == 0) {
                    x = 0;
                } else {
                    x = x + getDiff(x, chain.size() % 1500);
                }
            }

        }, "difficulty");

        var t2 = new Thread(null, () -> {

            while(!threadInterrupted.get()) {
                if(chain.size() % 1500 == 0) durationUntil1500thBlock.setPlain(0);
                durationUntil1500thBlock.getAndIncrement();
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    threadInterrupted.set(true);
                }
            }

        }, "duration");

        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

    }

    private int getDiff(int a, int b) {
        return Math.abs(a - b);
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
