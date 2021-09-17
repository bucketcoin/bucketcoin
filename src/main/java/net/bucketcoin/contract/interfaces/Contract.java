package net.bucketcoin.contract.interfaces;

/**
 * Represents a smart contract.
 * @implNote Contracts are required to form into {@link AcceptedContract}s upon acceptance.
 */
public interface Contract {

	/**
	 * Execution code for the Contract implementation runs in this method.
	 * @return whether the execution of the Contract is successful.
	 */
	boolean execute();

	/**
	 * Gets the version of the smart
	 * @return the versioning number
	 */
	int getVersion();

}
