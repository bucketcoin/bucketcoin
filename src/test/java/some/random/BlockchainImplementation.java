package some.random;

import net.bucketcoin.Bucketcoin;
import net.bucketcoin.block.Transaction;
import net.bucketcoin.node.Mempool;
import net.bucketcoin.node.Miner;
import net.bucketcoin.p2p.Node;

import java.nio.channels.MulticastChannel;
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
			Miner.getInstance().mine(g.getNonce(), Miner.MiningFramework.JavaCPU);
		}

	}

	private static void printHelp() {

		System.out.println("GenesisMiner\n\n" +
				"mine -> Starts mining blocks\n" +
				"private -> Prints private key\n" +
				"public -> Prints public key");

	}

}
