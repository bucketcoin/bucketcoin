/*
 *    Copyright 2021 The Bucketcoin Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.bucketcoin.collections;

import com.google.gson.Gson;
import lombok.Getter;
import net.bucketcoin.util.CryptoResources;
import static net.bucketcoin.util.SerializationResources.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * This class represents a 'block' in cryptography.
 */
public class HashChainBlock implements IBlock, Serializable {

	@Getter
	private final String hash;
	@Getter
	private final String prevHash;
	@Getter
	private final String prevHash2;
	@Getter
	protected final Object data;

	/**
	 * If a {@link HashChain} has no blocks yet, this class is to be used.
	 * @apiNote Trying to perform operations on this (null) block will throw an {@link
	 * IllegalAccessException}, as this block is intended to be unmodified.
	 */
	public static final class NullHashChainBlock extends HashChainBlock {

		private final String nHash;

		public NullHashChainBlock() {
			super("", "", "");
			nHash = this.calculateHash();
		}

		@Override
		public String getHash() {
			return nHash;
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
	public HashChainBlock(@NotNull HashChainBlock prevBlock, @NotNull Object data) {
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
		byte[] p2 = null;
		if(!(prevHash2 == null)) p2 = this.prevHash2.getBytes(getStandardCharset());
		byte[] c, k;
		var i = Objects.hash(this, this.toJson(), this.data);
		var digest = CryptoResources.getStandardDigest();
		c = DigestUtils.sha(String.valueOf(i));

		byte[] allByteArray;
		if(p2 != null) allByteArray = new byte[p.length + c.length + p2.length]; else
			 allByteArray = new byte[p.length + c.length];
		ByteBuffer buff = ByteBuffer.wrap(allByteArray);
		buff.put(p);
		buff.put(c);
		if(p2 != null) buff.put(p2);
		k = buff.array();
		buff.clear();

		digest.update(k);
		var f = digest.digest();
		return String.valueOf(getStandardCharset().decode(ByteBuffer.wrap(f)));

	}

	@Contract("-> new")
	public final byte @NotNull [] asBytes() {

		return SerializationUtils.serialize(this);

	}

	public static HashChainBlock fromBytes(byte[] bytes) {

		return SerializationUtils.deserialize(bytes);

	}

}