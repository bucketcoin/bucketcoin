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

package net.bucketcoin.p2p;

import com.google.gson.GsonBuilder;
import net.bucketcoin.block.Block;
import net.bucketcoin.block.Trade;
import net.bucketcoin.block.Transaction;
import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.NotNull;
import peerbase.PeerInfo;
import peerbase.PeerMessage;

import java.security.Key;

public class Broadcast {

    public static final String BLOCK = "BLCK";
    public static final String REQUEST_TRADE = "TRDE";
    public static final String TRANSACTION = "TNSC";
    public static final String INSERTPEER = "JOIN";
    public static final String LISTPEER = "LIST";
    public static final String PEERNAME = "NAME";
    public static final String QUERY = "QUER";
    public static final String QRESPONSE = "RESP";
    public static final String PEERQUIT = "QUIT";
    public static final String REPLY = "REPL";
    public static final String ERROR = "ERRO";

    private static final Node node = Node.getInstance();

    /**
     * Broadcasts a newly added block to the rest of the P2P network.
     * @param block The block that has been added to the blockchain.
     */
    public static void block(Block block) {
        PeerMessage p = new PeerMessage(BLOCK, new GsonBuilder().create().toJson(block));
        for(String peerKey : node.getPeerKeys()) {
            PeerInfo peerInfo = node.getPeer(peerKey);
            node.connectAndSend(peerInfo, BLOCK, p.getMsgData(), false);
        }
    }
    /**
     * Broadcasts a transaction (yet to be validated) to the rest of the P2P network.
     * @param transaction The transaction that has been validated.
     * @param publicKey The transaction sender's public key.
     * @param signature The signature of the transaction to be validated.
     * @see net.bucketcoin.node.Miner#pushBlock(Transaction, Key, byte[])
     */
    public static void transaction(@NotNull Transaction transaction, Key publicKey, byte[] signature) {
        PeerMessage peerMessage = new PeerMessage(TRANSACTION, transaction.toString());
    }
    /**
     * Broadcasts the newly requested trade to the rest of the P2P network.
     * @param trade The trade that has been requested.
     * @param publicKey The Trade request
     */
    public static void tradeRequest(Trade trade, Key publicKey, byte[] signature) {

    }

}
