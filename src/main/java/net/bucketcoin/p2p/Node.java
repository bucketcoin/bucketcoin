package net.bucketcoin.p2p;

import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import peerbase.*;

public final class Node extends peerbase.Node {

    private static Node n = new Node(0, new PeerInfo(1242), null);
    private static Wallet nodeWallet = null;
    /**
     * Returns the singleton Node for the {@link Wallet} provided.
     * @return The Node instance.
     * @apiNote If any methods called from this Node throw a {@link NullPointerException},
     * this indicates that the {@link Node#nodeWallet} has not been set. A node must be
     */
    public static Node getInstance() {
        return n;
    }

    public void init(Wallet wallet, @Range(from = 0, to = 9999) int port) {
        n = new Node(0, new PeerInfo(port), wallet);
    }

    public static Wallet getNodeWallet() {
        if(nodeWallet == null) {
            throw new IllegalStateException("Node is not initialized, call to init(Wallet) must be made");
        } else {
            return nodeWallet;
        }
    }

    private record Router(Node peer) implements RouterInterface {

        public @Nullable PeerInfo route(String peerID) {
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
