package net.bucketcoin.exception;

public class InsufficientBalanceException extends Exception {
	public InsufficientBalanceException(String message) {
		super(message);
	}

	public InsufficientBalanceException() {
		super();
	}
}
