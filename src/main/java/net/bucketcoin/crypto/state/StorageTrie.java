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

import com.google.gson.Gson;
import lombok.Getter;
import lombok.SneakyThrows;
import net.bucketcoin.p2p.Node;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class is a trie for storing contract data.
 */
public class StorageTrie {

	@Getter private final String address;
	private static final StorageTrie userTrie;

	static {
		if(Node.getNodeWallet() == null) throw new IllegalStateException("Node is not initialized, call to init(Wallet) must be made");
		userTrie = new StorageTrie(Node.getNodeWallet().getAddress());
	}

	/** @see net.bucketcoin.contract.proc.ContractFileOperations#getContractAsString(File)  **/
	private final HashSet<String> contractsActive = new HashSet<>(), contractsPending = new HashSet<>();

	/**
	 * Creates a StorageTrie unique to each address.
	 * @param address The wallet address.
	 */
	@SneakyThrows
	public StorageTrie(String address) {
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
		return DigestUtils.sha(toString().getBytes(StandardCharsets.ISO_8859_1));
	}

	public HashMap<Integer, String> getActiveContractsAsMap() {
		return getMapFromSet(contractsActive);
	}

	public HashMap<Integer, String> getPendingContractsAsMap() {
		return getMapFromSet(contractsPending);
	}

	/**
	 * Converts a {@link HashSet} to a {@link HashMap}
	 * @param objects The set to convert to a map.
	 * @param <E> The type to get a set from.
	 * @return The newly made {@link HashMap}.
	 * @apiNote The generated map functions like a list, with the key being the position.
	 * The position 0 is the first value, and the returned value <bold>may differ</bold>.
	 * @see HashSet
	 */
	private synchronized <E> HashMap<Integer, E> getMapFromSet(HashSet<E> objects) {
		int p = 0;
		HashMap<Integer, E> hashMap = new HashMap<>();
		for(E e : objects) {
			hashMap.put(p, e);
			++p;
		}
		return hashMap;
	}

}
