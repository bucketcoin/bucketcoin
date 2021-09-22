package net.bucketcoin.collections;

public interface Trimmable<E> {

	/**
	 * Trims the object to a shorter size, best used
	 * to keep a small storage. This method does two things
	 * to the implementor:<br><br>
	 *
	 * 1. Cut the size of the implementor, for example
	 * a full list of capacity 20 gets cut from index of 4, so the chain
	 * now has 15 elements remaining. <br>
	 * 1.5. If the parameter <code>reduceCapacity</code> is <code>true</code>,
	 * the new size of the implementor is reduced to the specified capacity.
	 *
	 * @return The trimmed object.
	 */
	E trim(int index, boolean reduceCapacity);

}
