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

package net.bucketcoin.crypto.state;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;
import org.iq80.leveldb.*;
import org.jetbrains.annotations.NotNull;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a World State Trie where global operations are called.
 * Wallet balances are updated here and not {@link net.bucketcoin.wallet.Wallet}.
 */
public final class StateTrie {

	private static final StateTrie adr = new StateTrie(); // This is a singleton class.
	private static DB database;

	public Map<? extends byte[], ? extends byte[]> asMap() {
		ImmutableMap.Builder<byte[], byte[]> mapBuilder = ImmutableMap.builder();
		while(database.iterator().hasNext()) {
			Map.Entry<? extends byte[], ? extends byte[]> next =  database.iterator().next();
			mapBuilder.put(next);
		}
		return mapBuilder.build();
	}

	public static StateTrie getInstance() {
		return adr;
	}

	@SneakyThrows
	private StateTrie() {
		var opt = new Options();
		opt.createIfMissing();
		database = factory.open(new File("BCKTAddressRecord"), opt);
		var thisUser = new AddressProperties(0, 0, StorageTrie.getUserTrie().toHash(), null);
		AtomicInteger k = new AtomicInteger(0);
		database.forEach(entry -> k.getAndIncrement());
		if(Node.getNodeWallet() == null) throw new IllegalStateException("Node is not initialized, call to init(Wallet) must be made");
		if(k.equals(new AtomicInteger(0))) database.put(Node.getNodeWallet().asBytes(), thisUser.asBytes());

	}

	public static AddressProperties queryAddress(byte[] address) {
		return AddressProperties.fromBytes(database.get(address, new ReadOptions().verifyChecksums(true)));
	}

	public static void addAccount(@NotNull String address, @NotNull AddressProperties addressProperties) {

		database.put(address.getBytes(StandardCharsets.ISO_8859_1), addressProperties.asBytes());

	}

	/**
	 * A record representing the properties of an address.
	 * @param nonce The number of transactions sent from the user.
	 * @param balance The BCKT holdings of the user.
	 * @param storageRoot The SHA-256 hash of the user's {@link StorageTrie}
	 * @param codeHash Code hash.
	 */
	public static record AddressProperties(int nonce, double balance, byte[] storageRoot, @Setter byte[] codeHash) {

		public byte[] asBytes() {
			return toString().getBytes(StandardCharsets.ISO_8859_1);
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
			return fromJSON(new String(bytes, StandardCharsets.ISO_8859_1));
		}

	}

}
