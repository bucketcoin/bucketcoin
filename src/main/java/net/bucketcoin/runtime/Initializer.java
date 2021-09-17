package net.bucketcoin.runtime;

import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * This class's initialize method should be called in an implementation's main class
 * before utilizing other classes. This class is non-final in order to allow
 * implementations to initialize other libraries / native methods.
 * @implNote <b>ALL IMPLEMENTATIONS MUST CALL {@link Initializer#initializeCore()}</b>.
 * This is because Bucketcoin itself needs to initialize core libraries.
 */
public class Initializer {

	private static final Initializer init = new Initializer();
	@Getter
	private static boolean hasInitialized = false;

	private Initializer() {
		//no instance
	}

	public static Initializer getInstance() {
		return init;
	}

	/**
	 * Initializes the core libraries of Bucketcoin.
	 * @throws IllegalStateException if core initialization has already completed.
	 */
	public final void initializeCore() throws IllegalArgumentException {
		if(hasInitialized) throw new IllegalStateException("Bucketcoin core initialization has already completed.");

		Security.addProvider(new BouncyCastleProvider());

		hasInitialized = true;
	}

}
