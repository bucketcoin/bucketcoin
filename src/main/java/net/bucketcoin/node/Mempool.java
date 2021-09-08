package net.bucketcoin.node;

import lombok.Getter;
import lombok.SneakyThrows;
import net.bucketcoin.crypto.Bucketcoin;
import net.bucketcoin.block.Block;
import net.bucketcoin.block.Transaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.crypto.IllegalBlockSizeException;
import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.logging.Logger;

public final class Mempool {

    private static final Mempool mempool = new Mempool();

    @Getter
    private final LinkedHashSet<Block> blocks = new LinkedHashSet<>(30);

    private Mempool() { /* This is a singleton class. */ }

    public static Mempool getInstance() {
        return mempool;
    }

    /**
     *
     * @return The {@link Block} with the highest gas fees.
     * @throws IllegalBlockSizeException if gas fees for the block are negative
     */
    @Contract(pure = true)
    public @Nullable Block getHighestGasPriceBlock() throws IllegalBlockSizeException {
        if(blocks.size() == 0) {
            return null;
        }
        blocks.add(Bucketcoin.getInstance().getGenesis());
        Block highestGasPrice = new Block("", new Transaction(0, "", "", 0));
        final Block origin = new Block("", new Transaction(0, "", "", 0));
        for(Block b: blocks) {

            if(highestGasPrice.getGasFee() < b.getGasFee()) {
                highestGasPrice = b;
            }

        }
        if(highestGasPrice.equals(origin)) {
            throw new IllegalBlockSizeException("Gas fee is negative");
        }
        return highestGasPrice;
    }

    public @Nullable Block getLowestGasPriceBlock() throws IllegalBlockSizeException {
        if(blocks.size() == 0) {
            return null;
        }
        blocks.add(Bucketcoin.getInstance().getGenesis());
        Block lowestGasPrice = new Block("", new Transaction(0, "", "", 1999999999));
        final Block origin = new Block("", new Transaction(0, "", "", 1999999999));
        for(Block b: blocks) {

            if(lowestGasPrice.getGasFee() > b.getGasFee()) {
                lowestGasPrice = b;
            }

        }
        if(lowestGasPrice.equals(origin)) {
            throw new IllegalBlockSizeException("Gas fee is negative");
        }
        return lowestGasPrice;
    }

    /**
     * Adds a block to the pool.
     * @param block The block to be added to the mempool.
     * @return Whether the block could be added.
     */
    @SneakyThrows
    public synchronized boolean addBlockToPool(Block block) {
        try {
            blocks.add(block);
            return true;
        } catch(IllegalStateException exception) {
            var a = getLowestGasPriceBlock();
            if(Objects.requireNonNull(a).getGasFee() <= getLowestGasPriceBlock().getGasFee()) {
                return false; // The block was rejected for the mempool.
            } else {
                blocks.remove(a);
                blocks.add(block);
                return true;
            }
        }
    }

    /**
     * Removes a block from the pool.
     * @param block The block to be added to the mempool.
     * @return Whether the block could be added.
     */
    @SneakyThrows
    public synchronized void removeBlockFromPool(Block block) {
        try {
            blocks.remove(block);
        } catch(Exception exception) {
            Logger.getGlobal().severe(exception.getLocalizedMessage());
        }
    }

    public synchronized Block takeRandomBlockFromPool() {
        final int x = new SecureRandom().nextInt(blocks.size());
        var blocks2 = blocks.toArray(Block[]::new);
        removeBlockFromPool(blocks2[x]);
        return blocks2[x];
    }

}
