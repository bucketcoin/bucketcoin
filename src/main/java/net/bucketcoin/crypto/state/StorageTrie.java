package net.bucketcoin.crypto.state;

import com.google.gson.Gson;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;

public class StorageTrie {

	private final String address;

	public StorageTrie(String address) {

		this.address = address;
	}

	public static StorageTrie getUserTrie() {
		return new StorageTrie(Node.nodeWallet.getAddress());
	}

	@Override
	public String toString() {
		return new Gson().toJson(this, StorageTrie.class);
	}

	public byte[] toHash() {
		return DigestUtils.sha(toString().getBytes(StandardCharsets.US_ASCII));
	}
}
