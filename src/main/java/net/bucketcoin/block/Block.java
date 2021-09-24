/*
 *    Copyright 2021 The Bucketcoin Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.bucketcoin.block;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.bucketcoin.collections.IBlock;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An instance of a block for the miner.
 */
public class Block implements Serializable, IBlock {

    @Getter
    private final int nonce = Math.round(new SecureRandom().nextInt() * 694206942);

    @NotNull
    private final Timestamp timestamp; // the block timestamp
    private final String prevHash; // parent hash
    @Getter private double GasFee; // the gas fee to pay
    private @NotNull List<Blockable> blockentries = new ArrayList<>(); // transactions to store
    private String stateRoot;
    private String storageRoot;
    private String transactionRoot;

    public Blockable[] getBlockEntries() {
        return blockentries.toArray(Blockable[]::new);
    }

    public Block(@NotNull String prevHash, @NotNull ArrayList<Blockable> transactions) {
        this.prevHash = prevHash;
        this.blockentries = transactions;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Block(@NotNull String prevHash, @NotNull Blockable... transactions) {
        this.prevHash = prevHash;
        this.blockentries.addAll(Arrays.asList(transactions));
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    {
        for(Blockable b : getBlockEntries()) GasFee += b.getGasFee();
    }

    public String getHash() {
        var a = new GsonBuilder().create();
        var b = a.toJson(this, Block.class);
        return DigestUtils.shaHex(b);
    }

    @Override
    public String toString() {
        var a = new GsonBuilder().create();
        return a.toJson(this, Block.class);
    }
}
