package net.bucketcoin.p2p;

import peerbase.*;

public final class Node extends peerbase.Node {

    private static final Node n = new Node(0, new PeerInfo(6942));

    public static Node getInstance() {
        return n;
    }

    private static class Router implements RouterInterface {
        private final Node peer;

        public Router(Node peer) {
            this.peer = peer;
        }

        public PeerInfo route(String peerid) {
            if (peer.getPeerKeys().contains(peerid)) return peer.getPeer(peerid);
            else return null;
        }
    }

    /**
     * Initialize this node with the given info and the specified
     * limit on the size of the peer list.
     *
     * @param maxPeers the maximum size of the peer list (0 means 'no limit')
     * @param info     the id and host/port information for this node
     */
    private Node(int maxPeers, PeerInfo info) {
        super(maxPeers, info);

        this.addRouter(new Router(this));

        this.addHandler(Broadcast.BLOCK, new BlockHandler(this));

    }
}
