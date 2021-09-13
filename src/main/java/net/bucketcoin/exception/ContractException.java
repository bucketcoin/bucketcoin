package net.bucketcoin.exception;

/**
 * This class represents an exception thrown due to {@link net.bucketcoin.contract} operations.
 */
public class ContractException extends Exception {

	public ContractException(String message) {
		super(message);
	}

	public ContractException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContractException(Throwable cause) {
		super(cause);
	}

	public ContractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
