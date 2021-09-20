package net.bucketcoin.collections;

import com.google.gson.Gson;
import lombok.Getter;
import net.bucketcoin.util.CryptoResources;
import static net.bucketcoin.util.SerializationResources.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * This class represents a 'block' in cryptography.
 * The hashes of this block are <i>"encrypted"</i>.
 * @param <E> The type to store.
 */
public class HashChainBlock<E> implements IBlock {

	@Getter
	private final String hash;
	@Getter
	private final String prevHash;
	@Getter
	private final String prevHash2;
	protected final E data;

	/**
	 * If a {@link HashChain} has no blocks yet, this class is to be used.
	 * @apiNote Trying to perform operations on this (null) block will throw an {@link
	 * IllegalAccessException}, as this block is intended to be unmodified.
	 * @param <S> The type to use upon block creation.
	 */
	public static final class NullHashChainBlock<S> extends HashChainBlock<S> {

		private final String nhash;

		@Contract(" -> new")
		public static <T> @NotNull NullHashChainBlock<T> getInstance() {
			return new NullHashChainBlock<>();
		}

		private NullHashChainBlock() {
			super("", "", "");
			nhash = this.calculateHash();
		}

		@Override
		public String getHash() {
			return nhash;
		}
	}

	/**
	 * Returns whether the block is a {@link NullHashChainBlock}
	 * or if this block's hash <i>inherits</i> its previous hash.
	 * @return Whether the block <i>inherits</i> a {@link NullHashChainBlock}'s
	 * hash, or if it <i>is, itself,</i> one.
	 */
	public boolean inheritsHashFromNullBlock() {

		return Objects.equals(this.prevHash2, "");

	}

	/**
	 * Creates a new 'block' from the previous block.
	 * @param prevBlock The previous block.
	 */
	public HashChainBlock(@NotNull HashChainBlock<E> prevBlock, E data) {
		this.data = data;
		this.prevHash = prevBlock.getHash();

		boolean v = File.class.isAssignableFrom(data.getClass());
		this.hash = calculateHash();
		this.prevHash2 = prevBlock.prevHash;
	}

	private HashChainBlock(@NotNull String hash, @NotNull String prevHash, @NotNull String prevHash2) {
		this.hash = hash;
		this.prevHash = prevHash;
		this.prevHash2 = prevHash2;
		this.data = null;
	}

	public String toJson() {
		return new Gson().toJson(this, HashChainBlock.class);
	}

	@Contract("-> new")
	protected @NotNull String calculateHash() {

		var p = this.prevHash.getBytes(getStandardCharset());
		var p2 = this.prevHash2.getBytes(getStandardCharset());
		byte[] c, k;
		var i = Objects.hash(this, this.toJson(), this.data);
		var digest = CryptoResources.getStandardDigest();
		c = DigestUtils.sha(String.valueOf(i));

		byte[] allByteArray = new byte[p.length + c.length + p2.length];
		ByteBuffer buff = ByteBuffer.wrap(allByteArray);
		buff.put(p);
		buff.put(c);
		buff.put(p2);
		k = buff.array();
		buff.clear();

		digest.update(k);
		var f = digest.digest();
		return new String(f, getStandardCharset());

	}

}