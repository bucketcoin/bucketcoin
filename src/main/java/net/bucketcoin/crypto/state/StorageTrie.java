package net.bucketcoin.crypto.state;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.SneakyThrows;
import net.bucketcoin.algorithm.merkletree.MerkleTree;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * This class is a trie for storing contract data.
 */
@SuppressWarnings("ClassCanBeRecord")
public class StorageTrie {

	private final String address;
	private static final StorageTrie userTrie = new StorageTrie(Node.nodeWallet.getAddress());
	private static MerkleTree storageMerkleTree;

	/**
	 * Creates a StorageTrie unique to each address.
	 * @param address The wallet address.
	 */
	@SneakyThrows
	public StorageTrie(String address) {
		StorageTrie.storageMerkleTree = new MerkleTree(MessageDigest.getInstance("SHA-256"));
		this.address = address;
	}

	public static StorageTrie getUserTrie() {
		return userTrie;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this, StorageTrie.class);
	}

	public byte[] toHash() {
		return DigestUtils.sha(toString().getBytes(StandardCharsets.US_ASCII));
	}



}
