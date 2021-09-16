package net.bucketcoin.central;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public final class CryptoResources {

	public static Cipher getStandardCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
		return Cipher.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
	}

}
