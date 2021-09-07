package net.bucketcoin.contract.interfaces;

import net.bucketcoin.block.Blockable;

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

}
