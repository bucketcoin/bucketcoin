package net.bucketcoin.crypto.state;

import com.google.gson.GsonBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bucketcoin.algorithm.merkletree.MerkleTree;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;
import org.iq80.leveldb.*;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;

public final class StateTrie {

	@SuppressWarnings("InstantiationOfUtilityClass")
	private static final StateTrie adr = new StateTrie(); // This is a singleton class.
	private static MerkleTree merkleTree;
	private static DB database;

	public static StateTrie getInstance() {
		return adr;
	}

	@SneakyThrows
	private StateTrie() {
		var opt = new Options();
		opt.createIfMissing();
		var db = factory.open(new File("BCKTAddressRecord"), opt);
		try {
			var thisUser = new AddressProperties(0, 0, StorageTrie.getUserTrie().toHash(), null);
			AtomicInteger k = new AtomicInteger(0);
			db.forEach(entry -> k.getAndIncrement());
			if(k.equals(new AtomicInteger(0))) db.put(Node.nodeWallet.asBytes(), thisUser.asBytes());
		} finally {
			StateTrie.database = db;
		}
		merkleTree = new MerkleTree(MessageDigest.getInstance("SHA-256"));
		// merkleTree.add();
	}

	public static AddressProperties queryAddress(byte[] address) {
		return AddressProperties.fromBytes(database.get(address, new ReadOptions().verifyChecksums(true)));
	}

	/**
	 * A record representing the properties of an address.
	 * @param nonce The number of transactions sent from the user.
	 * @param balance The BCKT holdings of the user.
	 * @param storageRoot The SHA-256 hash of the user's {@link StorageTrie}.
	 * @param codeHash Code hash.
	 */
	public static record AddressProperties(int nonce, double balance, byte[] storageRoot, @Setter byte[] codeHash) {

		public byte[] asBytes() {
			return toString().getBytes(StandardCharsets.US_ASCII);
		}

		@Override
		public String toString() {
			return new GsonBuilder().create().toJson(this, AddressProperties.class);
		}

		public String getHash() {
			return DigestUtils.shaHex(toString());
		}

		public static AddressProperties fromJSON(String json) {
			return new GsonBuilder().create().fromJson(json, AddressProperties.class);
		}

		public static AddressProperties fromBytes(byte[] bytes) {
			return fromJSON(new String(bytes, StandardCharsets.US_ASCII));
		}

	}

}
