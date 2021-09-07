package net.bucketcoin.contract.interfaces;

import net.bucketcoin.block.Blockable;

public interface Contract extends Blockable {

	/**
	 * Execution code for the Contract implementation runs in this method.
	 * @return whether the execution of the Contract is successful.
	 */
	boolean execute();

	int getVersion();

}
