package net.bucketcoin.runtime;

import lombok.Getter;
import net.bucketcoin.runtime.exception.InitializationException;
import net.bucketcoin.runtime.exception.NonConformingJDKError;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.security.*;

/**
 * This class is a utility class whose method {@link #initialize()} before any other implementor methods.
 *
 * @implSpec This class's initialize method should be called in an implementation's main class
 *  before utilizing other classes. This class is non-final in order to allow
 *  implementations to initialize other libraries / native methods.
 *
 * @implNote <b>ALL IMPLEMENTATIONS MUST CALL {@link Initializer#initializeCore()}</b>.
 * This is because Bucketcoin itself needs to initialize core libraries.
 */
public class Initializer {

	private static final Initializer init = new Initializer();
	@Getter
	private static boolean coreLibsInitialized = false;

	private Initializer() {
		//no instance
	}

	public static Initializer getInstance() {
		return init;
	}

	/**
	 * Initializes the core libraries of Bucketcoin.
	 * @throws IllegalStateException if core initialization has already completed.
	 * @implSpec This method has to be invoked by
	 */
	@MustBeInvokedByOverriders
	public final void initializeCore() throws IllegalArgumentException, NoSuchProviderException {
		if(coreLibsInitialized) throw new IllegalStateException("Bucketcoin core initialization has already completed.");

		try {
			Security.addProvider(new BouncyCastleProvider());
			Security.setProperty("crypto.policy", "unlimited");
			Policy.getPolicy().refresh();
			try {

				KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);

			} catch(NoSuchAlgorithmException e) { // unlikely to be thrown
				var s = """
						Algorithm EC is unavailable.
						This may be due to a violation of Article 1, Annex A (JDK Requirements ...), Bucketcoin Protocol,
						which states that the JDK must implement the "UNLIMITED" security policy.

						\tSTACK TRACE OF CAUGHT NoSuchAlgorithmException:
						""";
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(s);
				for(StackTraceElement stackTraceElement : e.getStackTrace()) {
					stringBuilder.append("\t").append(stackTraceElement.toString()).append("\n");
				}
				stringBuilder.append("\tEND OF STACK TRACE\n");
				stringBuilder.append("\n\tERROR STACK TRACE");

				throw new NonConformingJDKError(stringBuilder.toString());
			}
		} catch(SecurityException s) {
			throw new InitializationException("A security violation occurred (thrown SecurityException) with message " + s.getMessage());
		}

		coreLibsInitialized = true;
	}

	public void initialize() {

	}

}
