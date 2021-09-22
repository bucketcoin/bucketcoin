package net.bucketcoin.runtime.exception;

/**
 * This error is thrown when the JDK does not conform to the Bucketcoin protocol,
 * especially <i>Article 1 of the Bucketcoin Protocol (Annex A)</i> where the JDK must
 * have an "unlimited" security policy for the JCE.
 */
public class NonConformingJDKError extends Error {

	public NonConformingJDKError() {
		super();
	}

	public NonConformingJDKError(String message) {
		super(message);
	}

	public NonConformingJDKError(String message, Throwable cause) {
		super(message, cause);
	}


	public NonConformingJDKError(Throwable cause) {
		super(cause);
	}

	protected NonConformingJDKError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
