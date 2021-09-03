package net.bucketcoin.p2p;

import com.google.gson.GsonBuilder;
import net.bucketcoin.block.Block;
import net.bucketcoin.block.Transaction;
import net.bucketcoin.node.Mempool;
import peerbase.HandlerInterface;
import peerbase.PeerConnection;
import peerbase.PeerMessage;

public class TransactionHandler implements HandlerInterface {
	/**
	 * Invoked when a peer connects and sends a message to this node.
	 */
	@Override
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {

		Block block = new Block("",
				new GsonBuilder().disableHtmlEscaping()
						.create().fromJson
								(msg.getMsgData(), Transaction.class));

		Mempool.getInstance().addBlockToPool(block);
	}
}
