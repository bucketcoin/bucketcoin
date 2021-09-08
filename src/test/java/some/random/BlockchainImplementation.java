package some.random;

import net.bucketcoin.crypto.Bucketcoin;
import net.bucketcoin.node.Miner;
import net.bucketcoin.p2p.Node;

import java.util.Objects;

public class BlockchainImplementation {

	public static void main(String[] args) {

		if(args.length == 1) {
			if(Objects.equals(args[0], "-h")) {
				printHelp();
			} else if(Objects.equals(args[0], "mine")) {
				// Miner.getInstance().
			}
		} else {
			var n = Node.getInstance();
			var g = Bucketcoin.getInstance().getGenesis();
			Miner.getInstance().mine(g.getNonce());
		}

	}

	private static void printHelp() {

		System.out.println("""
				GenesisMiner

				mine -> Starts mining blocks
				private -> Prints private key
				public -> Prints public key""");

	}

}
