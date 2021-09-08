package net.bucketcoin.block;

import java.io.Serializable;

/**
 *  An interface to mark objects that can be put in blocks.
 */
public interface Blockable extends Serializable {

    /**
     * Returns the gas fee in 0.000001 BCKT.
     */
    public double getGasFee();

    /**
     * Converts the {@linkplain Blockable} into a JSON string.
     */
    public String toString();

}
