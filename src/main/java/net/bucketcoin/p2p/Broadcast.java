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
     */
    public static void trade(Trade trade) {

    }

}
