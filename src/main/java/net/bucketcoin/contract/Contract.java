package net.bucketcoin.contract;

import net.bucketcoin.block.Blockable;

/**
 * Represents a smart contract.
 * @implNote Contracts are required to form into {@link AcceptedContract}s upon acceptance.
 */
public interface Contract extends Blockable {



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

	/**
	 * Returns the gas fee in 0.000001 BCKT.
	 */
	@Override
	double getGasFee();
}
