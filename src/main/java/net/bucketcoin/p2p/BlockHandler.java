package net.bucketcoin.p2p;

import com.google.gson.GsonBuilder;
import net.bucketcoin.Bucketcoin;
import net.bucketcoin.block.Block;
import net.bucketcoin.node.Mempool;
import peerbase.HandlerInterface;
import peerbase.Node;
import peerbase.PeerConnection;
import peerbase.PeerMessage;

public class BlockHandler implements HandlerInterface {

    private Node node;

    public BlockHandler(Node node) {
        this.node = node;
    }

    /**
     * Invoked when a peer connects and sends a message to this node.
     */
    @Override
    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {

        var data = msg.getMsgData();
        var gson = new GsonBuilder().create();
        final Block b = gson.fromJson(data, Block.class);
        Mempool.getInstance().removeBlockFromPool(b);
        Bucketcoin.getInstance().chain.push(b);
        Broadcast.block(b);

    }
}
