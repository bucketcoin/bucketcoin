/*
Copyright (c) 2020 richpl

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package net.bucketcoin.algorithm.merkletree;

import java.util.List;

/**
 * Represents a Merkle Tree leaf, consisting of a list
 * of blocks of arbitrary data. The arbitrary data in each block
 * is represented as a byte array.
 */
@SuppressWarnings("ClassCanBeRecord")
public class Leaf
{
	// The data to be stored in this node
	private final List<byte[]> dataBlock;

	/**
	 * Initialises the leaf node, which consists
	 * of the specified block of data.
	 *
	 * @param dataBlock Data block to be placed in the leaf node
	 */
	public Leaf(final List<byte[]> dataBlock)
	{
		this.dataBlock = dataBlock;
	}

	/**
	 * @return The data block associated with this leaf node
	 */
	public List<byte[]> getDataBlock()
	{
		return (dataBlock);
	}

	/**
	 * Returns a string representation of the specified
	 * byte array, with the values represented in hex. The
	 * values are comma separated and enclosed within square
	 * brackets.
	 *
	 * @param array The byte array
	 *
	 * @return Bracketed string representation of hex values
	 */
	private String toHexString(final byte[] array)
	{
		final StringBuilder str = new StringBuilder();

		str.append("[");

		boolean isFirst = true;
		for(final byte b : array) {
			if(isFirst) {
				//str.append(Integer.toHexString(i));
				isFirst = false;
			} else {
				//str.append("," + Integer.toHexString(i));
				str.append(",");
			}

			final int hiVal = (b & 0xF0) >> 4;
			final int loVal = b & 0x0F;
			str.append((char) ('0' + (hiVal + (hiVal / 10 * 7))));
			str.append((char) ('0' + (loVal + (loVal / 10 * 7))));
		}

		str.append("]");

		return(str.toString());
	}

	/**
	 * Returns a string representation of the data block
	 */
	public String toString()
	{
		final StringBuilder str = new StringBuilder();

		for(byte[] block: dataBlock)
		{
			str.append(toHexString(block));
		}

		return(str.toString());
	}

}
