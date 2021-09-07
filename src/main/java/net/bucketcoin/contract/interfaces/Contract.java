package net.bucketcoin.contract.interfaces;

import net.bucketcoin.block.Blockable;

public interface Contract extends Blockable {

	/**
	 * Execution code for the Contract implementation runs in this method.
	 * @return whether the execution of the Contract is successful.
	 */
	boolean execute();

	/**
	 * Returns the gas fee in 0.000001 BCKT.
	 */
	@Override
	double getGasFee();

	int getVersion();

}
