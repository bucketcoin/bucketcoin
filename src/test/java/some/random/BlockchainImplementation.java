package some.random;

import lombok.SneakyThrows;
import net.bucketcoin.collections.HashChain;
import net.bucketcoin.collections.HashChainBlock;
import net.bucketcoin.p2p.Node;
import net.bucketcoin.runtime.Initializer;
import net.bucketcoin.collections.HashChains;
import net.bucketcoin.wallet.Wallet;

import java.io.File;
import java.util.Objects;

public class BlockchainImplementation {

	// for now, users just play around in a sandbox
	@SneakyThrows
	public static void main(String[] args) {

		//var bckt = Bucketcoin.getInstance();
		Initializer.getInstance().initializeCore();

		var minerWallet = new Wallet();
		var n = Node.getInstance(); // init
		n.init(minerWallet, 8080); // 8080 is a testing port

		var testWallet = new Wallet();
		var testWallet2 = new Wallet();
		// testWallet.sendMessage(new SendBCKT(testWallet, testWallet2, 5, 100));

		if(args.length == 1) {
			if(Objects.equals(args[0], "-h")) {
				printHelp();
			}
		} else {

			System.out.println("HASHCHAIN TESTING");

			HashChain<String> stringChain = new HashChain<>();
			stringChain.addFirst("1");
			stringChain.addFirst("2");
			stringChain.addFirst("3");
			stringChain.addFirst("4");
			stringChain.addFirst("5");
			stringChain.addFirst("6");
			stringChain.addFirst("7");

			// System.out.println(stringChain.validate() + "\n");
			int k = 0;
			System.out.println("-------------------------------------------------------");
			for(HashChainBlock block : stringChain.toBlockArray()) {


				if(!(block == null)) {
					String a = "<-- NOT APPLICABLE -->", b = "<-- NOT APPLICABLE -->";
					if(!block.getPrevHash().equals("")) a = block.getPrevHash();
					if(!block.getPrevHash2().equals("")) b = block.getPrevHash2();
					System.out.println("BLOCK NUMBER " + k);
					// System.out.println("CONTENT " + block.getData().toString());
					System.out.println("HASH -> " + block.getHash());
					System.out.println("PREVIOUS HASH -> " + a);
					System.out.println("SECOND PREVIOUS HASH ->  " + b);
					System.out.println("-------------------------------------------------------");
				k++; }

			}

			HashChains.storeChain(stringChain, new File("ChangChang.txt"), false);

			//var n = Node.getInstance();
			// var g = Bucketcoin.getInstance().getGenesis();
			// Miner.getInstance().mine(g.getNonce());
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
