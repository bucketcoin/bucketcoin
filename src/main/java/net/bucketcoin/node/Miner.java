package net.bucketcoin.node;

import static net.bucketcoin.algorithm.DifficultyAlgorithm.*;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import net.bucketcoin.block.Blockable;
import net.bucketcoin.crypto.Bucketcoin;
import net.bucketcoin.block.Block;
import net.bucketcoin.block.Transaction;
import net.bucketcoin.p2p.Broadcast;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;


import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Example miner.
 */
public class Miner {

    private static final Miner miner = new Miner();

    private Miner() {
        //no instance
    }

    public static Miner getInstance() {
        return miner;
    }

    @SneakyThrows
    public void pushBlock(@NotNull Transaction transaction, Key senderPublicKey, byte[] signature) {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, senderPublicKey);
        final byte[] dec = c.doFinal(signature);
        if(Arrays.equals(dec, DigestUtils.shaHex(transaction.toString()).getBytes(StandardCharsets.ISO_8859_1))) {

            var block = new Block(Bucketcoin.getInstance().getLastBlock().getHash(), new ArrayList<>() {{
                add(transaction);
                if(Node.getNodeWallet() == null) throw new IllegalStateException("Node is not initialized, call to init(Wallet) must be made");
                add(new Transaction(6, "rewards", Node.getNodeWallet().getAddress(), 0));
            }});
            this.mine(block.getNonce());
            System.out.println(new Gson().newBuilder().create().toJson(block));
            Bucketcoin.getInstance().chain.push(block);
            Broadcast.block(block);

        }
    }

    @SneakyThrows
    public void pushBlock(Key senderPublicKey, byte[] signature, Blockable... transactions) {
        for(Blockable transaction : transactions) {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, senderPublicKey);
            final byte[] dec = c.doFinal(signature);
            if(Arrays.equals(dec, DigestUtils.shaHex(transaction.toString()).getBytes(StandardCharsets.ISO_8859_1))) {

                var block = new Block(Bucketcoin.getInstance().getLastBlock().getHash(), new ArrayList<>() {{
                    add(transaction);
                    if(Node.getNodeWallet() == null) throw new IllegalStateException("Node is not initialized, call to init(Wallet) must be made");
                    add(new Transaction(6, "rewards", Node.getNodeWallet().getAddress(), 0));
                }});
                this.mine(block.getNonce());
                System.out.println(new Gson().newBuilder().create().toJson(block));
                Bucketcoin.getInstance().chain.push(block);
                Broadcast.block(block);

            }
        }
    }

    /**
     * Mines the block.
     * @param nonce The nonce.
     */
    public void mine(int nonce) {

        int sol = 1;
        StringBuilder difficultyStringBuilder = new StringBuilder();

        for(int i = 0; i < getDifficulty(); i++) {
            difficultyStringBuilder.append('0');
        }

        while(true) {
            var hash = DigestUtils.md5Hex(String.valueOf(nonce + sol));
            System.out.println(hash); // DEBUG PURPOSES
            if(hash.startsWith(difficultyStringBuilder.toString())) {
                Logger.getGlobal().info("Solution accepted : " + hash);
                return;
            }
            sol++;

        }

    }

}