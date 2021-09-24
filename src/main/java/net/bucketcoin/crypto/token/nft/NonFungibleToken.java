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

package net.bucketcoin.crypto.token.nft;

import lombok.SneakyThrows;
import net.bucketcoin.util.CryptoResources;
import net.bucketcoin.util.SerializationResources;
import net.bucketcoin.exception.InsufficientBalanceException;
import net.bucketcoin.wallet.Wallet;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.Key;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;

public final class NonFungibleToken {

	private final Key identifier; // Bucketcoin is spent to generate a Key.
	private final File file;
	private final String minterAddress;
	private final UUID serial;
	private final Timestamp generated;

	@SneakyThrows
	private NonFungibleToken(@NotNull File file, String minterAddress) {

		this.file = file;
		this.identifier = CryptoResources.getStandardSymmetricKeyGenerator().generateKey();
		this.minterAddress = minterAddress;
		this.serial = UUID.randomUUID();
		this.generated = Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));

	}

	@SneakyThrows
	private NonFungibleToken(@NotNull File file, String minterAddress, Key key) {

		this.file = file;
		this.identifier = key;
		this.minterAddress = minterAddress;
		this.serial = UUID.randomUUID();
		this.generated = Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));

	}

	public static @NotNull NonFungibleToken mint(@NotNull File file, @NotNull Wallet minterWallet) throws InsufficientBalanceException {
		var a = minterWallet.hasEnough(0.032);
		if(!a) throw new InsufficientBalanceException(); else
			return new NonFungibleToken(file, minterWallet.getAddress());
	}

	public static NonFungibleToken[] mint(@NotNull File file, int copies, @NotNull Wallet minterWallet) throws InsufficientBalanceException, IllegalArgumentException {
		if(copies <= 0) throw new IllegalArgumentException("Number of copies CANNOT be negative!");
		var a = minterWallet.hasEnough(0.032 * copies);
		if(!a) throw new InsufficientBalanceException(); else {
			ArrayList<NonFungibleToken> nonFungibleTokens = new ArrayList<>(copies);
			var k = CryptoResources.getStandardSymmetricKeyGenerator().generateKey();
			for(int i = 0; i < copies; i++) nonFungibleTokens.add(
					new NonFungibleToken(file, minterWallet.getAddress(), k)
			);
			return nonFungibleTokens.toArray(NonFungibleToken[]::new);
		}
	}

	/**
	 * Converts the NFT instance to a String. This is done by hashing the standard {@link Object#toString()}
	 * method using SHA-256. This method works as a standard {@link Object#toString()} like<br><br>
	 * {@code "NonFungibleToken{" + "identifier=" + identifier + ", file=" + convertFileToBase64() + ", minter=" + minterAddress + ", serial=" + serial.toString() + '}';} <br><br>
	 * with the additional hashing of the representation above in the following way: <br><br>
	 * {@code var digest =  CryptoResources.getStandardDigest();
	 * 		digest.update(detail.getBytes(SerializationResources.getStandardCharset()));
	 * 		return new String(digest.digest(), SerializationResources.getStandardCharset());}
	 * @return The {@link String} representation of this non-fungible token.
	 */
	@Contract(" -> new")
	@Override
	public @NotNull String toString() {
		var detail = "NonFungibleToken{" + "identifier=" + identifier.toString() + ", file=" + convertFileToBase64() + ", minter=" + minterAddress + ", serial=" + serial.toString() + ", gen=" + generated.toString() + '}';
		var digest =  CryptoResources.getStandardDigest();
		digest.update(detail.getBytes(SerializationResources.getStandardCharset()));
		return new String(digest.digest(), SerializationResources.getStandardCharset());
	}

	@SneakyThrows
	@Contract(pure = true)
	private String convertFileToBase64() {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		return Base64.getEncoder().encodeToString(fileContent);
	}

	/**
	 * This method checks for equality between two {@link NonFungibleToken}s.
	 * @param nft The {@link NonFungibleToken} to compare.
	 * @return Whether this instance matches with the parameter provided.
	 */
	@Contract(pure = true)
	public boolean equals(@NotNull NonFungibleToken nft) {

		if(nft.toString().equals(this.toString())) return true;
		else {
			var a = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.MINUTE);
			var b = generated.toLocalDateTime().get(ChronoField.MINUTE_OF_DAY);
			return (a - b) <= 5;
		}

	}
}