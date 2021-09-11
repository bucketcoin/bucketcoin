package net.bucketcoin.crypto.state;

import com.google.gson.GsonBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;
import org.iq80.leveldb.*;
import org.jetbrains.annotations.Nullable;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class StateTrie {

	@SuppressWarnings("InstantiationOfUtilityClass")
	private static final StateTrie adr = new StateTrie(); // This is a singleton class.
	private static DB database;

	public static StateTrie getInstance() {
		return adr;
	}

	@SneakyThrows
	private StateTrie() {
		var opt = new Options();
		opt.createIfMissing();
		try(var db = factory.open(new File("BCKTAddressRecord"), opt)) {
			var thisUser = new AddressProperties(0, 0, StorageTrie.getUserTrie().toHash(), null);
			AtomicInteger k = new AtomicInteger(0);
			db.forEach(entry -> k.getAndIncrement());
			// check for empty database (for construction)
			if(k.equals(new AtomicInteger(0))) {
				db.put(Node.nodeWallet.asBytes(), thisUser.asBytes());
			}
			StateTrie.database = db;
		}

	}

	public static AddressProperties queryAddress(byte[] address) {
		return AddressProperties.fromBytes(database.get(address, new ReadOptions().verifyChecksums(true)));
	}

	/**
	 * A record representing the properties of an address.
	 * @param nonce The number of transactions sent from the user.
	 * @param balance The balance of BCKt of the user.
	 * @param storageRoot The SHA-256 hash of the user's  {@link StorageTrie}.
	 * @param codeHash The hash of the state trie.
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
