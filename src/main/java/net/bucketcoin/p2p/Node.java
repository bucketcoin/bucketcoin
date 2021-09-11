package net.bucketcoin.p2p;

import net.bucketcoin.wallet.Wallet;
import peerbase.*;

public final class Node extends peerbase.Node {

    private static final Node n = new Node(0, new PeerInfo(6942), null);
    public static Wallet nodeWallet = null;

    /**
     * Returns the singleton Node for the {@link Wallet} provided.
     * @param wallet The wallet to associate the node with. Once set, cannot be changed.
     * @return The wallet instance.
     */
    public static Node getInstance(Wallet wallet) {
        if(nodeWallet == null) nodeWallet = wallet;
        return n;
    }

    public static Node getInstance() {
        if(nodeWallet == null) throw new IllegalStateException("Wallet has not been set.");
        return n;
    }

    private record Router(Node peer) implements RouterInterface {

        public PeerInfo route(String peerID) {
            if(peer.getPeerKeys().contains(peerID)) {
                return peer.getPeer(peerID);
            } else {
                return null;
            }
        }
    }

    /**
     * Initialize this node with the given info and the specified
     * limit on the size of the peer list.
     *
     * @param maxPeers the maximum size of the peer list (0 means 'no limit').
     * @param info     the id and host/port information for this node.
     * @param nodeWallet The {@link Wallet} associated with the node.
     */
    private Node(int maxPeers, PeerInfo info, final Wallet nodeWallet) {
        super(maxPeers, info);
        Node.nodeWallet = nodeWallet;

        this.addRouter(new Router(this));

        this.addHandler(Broadcast.BLOCK, new BlockHandler(this));

    }
}
