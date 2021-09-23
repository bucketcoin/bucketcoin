package net.bucketcoin.util;

import net.bucketcoin.collections.HashChain;
import net.bucketcoin.collections.HashChainBlock;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@SuppressWarnings("UnusedReturnValue")
public final class HashChains {



	/**
	 * Stores the HashChain in a file.
	 * @param hashChain The HashChain to store.
	 * @param file The file to store it in.
	 * @param checkValid Whether the chain should be validated. If it is not valid, <code>false</code>
	 *                   will be returned and the chain is not stored.
	 * @param <E> The HashChain type parameter.
	 * @return Whether the HashChain was stored.
	 * @throws IOException if an I/O error occurs.
	 */
	public static <E> boolean storeChain(@NotNull HashChain<E> hashChain, @NotNull File file, boolean checkValid) throws IOException {

		if(!hashChain.validate() && checkValid) return false;
		var ba = hashChain.toBlockArray();
		if(!file.exists()) //noinspection ResultOfMethodCallIgnored
			file.createNewFile();

		try(BufferedWriter bufferedWriter = new BufferedWriter(
				new FileWriter(file)
		)) {
			int i = 0;
			for(HashChainBlock h : ba) {

				if(h == null) continue;

				var b = h.asBytes();
				var s = new String(b, SerializationResources.getStandardCharset()) + '\n';
				bufferedWriter.write(s.toCharArray());
				i++;

			}
		}

		return true;

	}

}
