package some.random;

import lombok.SneakyThrows;
import net.bucketcoin.block.Transaction;
import net.bucketcoin.crypto.Bucketcoin;
import net.bucketcoin.message.SendBCKT;
import net.bucketcoin.node.Miner;
import net.bucketcoin.p2p.Node;
import net.bucketcoin.wallet.Wallet;

import java.util.Objects;

public class BlockchainImplementation {

	// for now, users just play around in a sandbox
	@SneakyThrows
	public static void main(String[] args) {

		//var bckt = Bucketcoin.getInstance();

		var minerWallet = new Wallet();
		Node.getInstance(minerWallet); // init

		var testWallet = new Wallet();
		var testWallet2 = new Wallet();
		testWallet.sendMessage(new SendBCKT(testWallet, testWallet2, 5, 100));

		if(args.length == 1) {
			if(Objects.equals(args[0], "-h")) {
				printHelp();
			} else if(Objects.equals(args[0], "mine")) {
				// Miner.getInstance().mine(69420);
			}
		} else {
			//var n = Node.getInstance();
			var g = Bucketcoin.getInstance().getGenesis();
			Miner.getInstance().mine(g.getNonce());
		}

	}

	private static void printHelp() {

		System.out.println("""
				GenesisMiner And Wallet

				mine -> Starts mining blocks
				private -> Prints private key
				public -> Prints public key""");

	}

}
