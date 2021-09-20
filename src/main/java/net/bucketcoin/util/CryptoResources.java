package net.bucketcoin.util;

import lombok.SneakyThrows;
import net.bucketcoin.wallet.Wallet;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * This class is a centralized resource class that holds
 * necessary cryptography objects.
 * @apiNote All instances returned are 'initialized', except
 * {@link CryptoResources#getStandardCipher()} and {@link
 * CryptoResources#getStandardDigest()}.
 */
public final class CryptoResources {

	public static final int KEY_SIZE = 512;
	public static final String HASH_ALGORITHM = "SHA-256";
	public static final String ALGORITHM = "";

	public enum AlgorithmType {

		RSA,
		EC

	}

	private static final SHA256.KeyGenerator sha256KeyGenerator = new SHA256.KeyGenerator() {{
		engineInit(KEY_SIZE, new SecureRandom());
	}};

	@Contract(" -> new")
	public static @NotNull Cipher getStandardCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
		return Cipher.getInstance("RSA");
	}

	/**
	 * Retrieves the KeyGenerator used for Bucketcoin.
	 * @return the standard key generator.
	 * @apiNote <b>THIS SHOULD NOT BE USED TO GENERATE WALLET KEYS!</b> For that,
	 * use {@link Wallet#Wallet()}. Symmetric key generators are used in Bucketcoin for
	 * generating NFT identifiers and their serial numbers.
	 */
	@SneakyThrows
	public static @NotNull KeyGenerator getStandardSymmetricKeyGenerator() {
		var k = KeyGenerator.getInstance("EC");
		k.init(KEY_SIZE);
		return k;
	}

	@SneakyThrows
	public static @NotNull MessageDigest getStandardDigest() {
		return MessageDigest.getInstance(HASH_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
	}

	// public static ECKeyPairGenerator getStandardKeyPairGenerator() {
	//	return ecKeyPairGenerator;
	//}

	public static KeyPairGenerator getStandardKeyPairGenerator() throws NoSuchAlgorithmException {
		return KeyPairGenerator.getInstance("EC");
	}


}
