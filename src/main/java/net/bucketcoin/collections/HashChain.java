/*
 *    Copyright 2021 The Bucketcoin Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.bucketcoin.collections;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * This class is meant to provide similar security of
 * a 'Merkle tree', however using a storage using chains.
 * This is implemented by using (not extending) {@link Vector}.
 * Each element added will be converted to a {@link HashChainBlock} and
 * appended to the array {@link #data}.<br><br>
 *
 * This class is similar to a {@link Queue}, which uses the FIFO approach.
 * @param <E> The type to store.
 */
public class HashChain<E> implements
						  Cloneable,
						  Serializable,
						  Trimmable<HashChain<E>> {

	private final boolean initWithNullBlock;
	@Getter private int structuralModificationCount = 0;
	private final int arrayIncrement;
	protected HashChainBlock[] data;
	private int count = 0;

	/**
	 * Constructs a HashChain with a capacity of 50 elements and the specified increment.
	 * @param increment How much the backing array used by this class
	 *                  should be increased in the event of no available
	 *                  slots to place an added element into.
	 *
	 * @throws IllegalArgumentException if <code>increment < 0</code> or <code>capacity < 1</code>.
	 *
	 * @apiNote This class is immediately initialized with a {@link HashChainBlock.NullHashChainBlock}.
	 */
	public HashChain(int increment) {
		this(increment, 50);
	}

	protected void setModCount(int mc) {
		if(mc < 0) throw new IllegalArgumentException();
		this.structuralModificationCount = mc;
	}

	/**
	 * Constructs a HashChain with the specified capacity and increment.
	 * @param increment How much the backing array used by this class
	 *					should be increased in the event of no available
	 * 					slots to place an added element into.
	 *
	 * @param capacity How many elements that the backing array can store.
	 * @throws IllegalArgumentException if <code>increment < 0</code> or <code>capacity < 1</code>.
	 *
	 * @apiNote This class is immediately initialized with a {@link HashChainBlock.NullHashChainBlock}.
	 */
	public HashChain(int increment, int capacity) {

		this(increment, capacity, true);

	}

	public int getArrayCapacity() { return data.length; }

	/**
	 * Constructs a HashChain with the specified capacity and increment.
	 * @param increment How much the backing array used by this class
	 *					should be increased in the event of no available
	 * 					slots to place an added element into.
	 *
	 * @param capacity How many elements that the backing array can store.
	 * @param initWithNullBlock Whether the chain should be initialized with a null block. While this will affect
	 *                          the number of elements, it will not affect the backing array's capacity.
	 * @throws IllegalArgumentException if <code>increment < 0</code> or <code>capacity < 1</code>.
	 */
	public HashChain(int increment, int capacity, boolean initWithNullBlock) {
		this.initWithNullBlock = initWithNullBlock;

		if(increment < 0 || capacity < 1) throw new IllegalArgumentException();

		this.arrayIncrement = increment;

		if(initWithNullBlock) {
			this.data = new HashChainBlock[capacity + 1];
			this.add(new HashChainBlock.NullHashChainBlock(), data, 0);
		} else this.data = new HashChainBlock[capacity];
	}



	/**
	 * Constructs a HashChain with a capacity of 50 elements and an increment of 1.
	 * @apiNote This class is immediately initialized with a {@link HashChainBlock.NullHashChainBlock}.
	 */
	public HashChain() {
		this(1);
	}

	public void addFirst(@NotNull E e) {

		var b = toBlock(e);
		add(b, data, count);

	}

	/**
	 * This is a helper method to add a {@link HashChainBlock}.
	 * @param e The element to add.
	 * @param eData The data array to add to.
	 * @param i The index to add the element to.
	 */
	protected void add(@NotNull HashChainBlock e, Object @NotNull [] eData, int i) {

		System.out.println("test");
		if(i == eData.length) grow();
		data[i] = e;
		count = i + 1;

	}


	@Contract("_ -> new")
	private @NotNull HashChainBlock toBlock(E e) {

		return new HashChainBlock(getPreviousBlock(), e);

	}

	private HashChainBlock getPreviousBlock() {

		return data[count - 1];

	}

	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		try {
			var k = getIndex((E) o);
			var willThrowExceptionIfNotContained = (k - 1) < 0; // confirmed to be false, try to cause ClassCastException
			if(willThrowExceptionIfNotContained) throw new InternalError("how is an array element's index negative?");
		} catch(NoSuchElementException | ClassCastException exception) {
			return false;
		}
		return true;
	}

	/**
	 * This method validates the {@link HashChain}'s integrity by getting previous hashes.
	 * In order to avoid implementations of a {@link HashChain} from falsifying the integrity
	 * of this chain, this method is a <code>final</code> method. This is done by the following method:<br><br>
	 * <code>for(Object o : data) {</code><br><br>
	 * 1. If <code>o</code>'s {@link HashChainBlock#inheritsHashFromNullBlock()} returns false,
	 * compare the previous HashChainBlock's hash to <code>o</code>'s {@link HashChainBlock#getPrevHash()}.<br><br>
	 * 2.
	 * @return Whether the chain is valid.
	 */
	public final synchronized boolean validate() {

		int index = 0;

		var k = data;

		for(HashChainBlock o : data) {

			if(o != null) {

				if(!o.inheritsHashFromNullBlock()) { // ignore 2nd prevHash if it inherits a hash from a NullHashChainBlock.

					if(!o.getPrevHash2().equals(
							k[index - 2].getHash()
					)) return false;
				} else if(index != 0) {
					if(!o.getPrevHash().equals(
							k[index - 1].getHash()
					)) return false;
				}
			}

			index++;



		}

		return true;

	}

	/**
	 * Returns the index for the specified {@link E}.
	 * @param e The {@link E} to search for.
	 * @return The {@link E}'s index.
	 * @throws NoSuchElementException if no such {@link E} exists
	 * in the chain.
	 */
	public int getIndex(E e) throws NoSuchElementException {

		int k = 0;

		for(HashChainBlock o : data) {
			if(o == null) continue;
			if(o.data == e) return k;
			k++;
		}
		return -1;

	}

	private int getIndex(HashChainBlock hashChainBlock) throws NoSuchElementException {

		int k = 0;

		for(HashChainBlock o : data) {
			if(o == null) continue;
			if(o == hashChainBlock) return k;
			k++;
		}
		return -1;
	}

	public boolean remove(Object o) {
		return this.removeFirst() != null;
	}

	@SuppressWarnings("unchecked")
	public E removeFirst() {

		if(count == 0) throw new IllegalStateException();
		final var c = count;
		var e = data[c];
		data[c] = null;
		count--;
		return (E) e.data;

	}

	public E pollFirst() {
		try {
			return removeFirst();
		} catch(IllegalStateException i) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public E getFirst() {
		if(count == 0) throw new IllegalStateException();
		return (E) data[count].data;
	}

	public E peekFirst() {
		try {
			return getFirst();
		} catch(IllegalStateException i) {
			return null;
		}
	}

	/**
	 * Grows the array size by the specified {@link #arrayIncrement}.
	 * Unlike other implementations of {@link Collection}, this directly modifies the array.
	 * <br><br>
	 * This is accomplished by creating a new array with the size
	 * {@code data.length} (current length) {@code + } {@link #arrayIncrement},
	 * then copying the old array's content to the new array, assigning the new
	 * array to the variable (formerly) holding the old array.
	 */
	private synchronized void grow() {

		HashChainBlock[] blocks = new HashChainBlock[data.length + arrayIncrement];
		System.arraycopy(data, 0, blocks, 0, data.length);
		this.data = blocks;
		structuralModificationCount++;

	}

	public synchronized boolean add(@NotNull E e) {
		try {
			this.addFirst(e);
			return true;
		} catch(Exception ignore) {
			return false;
		}
	}

	public int size() {
		return count;
	}

	public int increment() { return arrayIncrement; }

	@SuppressWarnings({"MethodDoesntCallSuperMethod", "unchecked"})
	public HashChain<E> clone() {

		try {

			var clone = new HashChain<>();
			Object[] blocks;
			blocks = clone.data;
			System.arraycopy(data, 0, blocks, 0, data.length);
			clone.structuralModificationCount = 0;
			return (HashChain<E>) clone;

		} catch(ClassCastException ignore) {

			throw new InternalError("Failed to perform an unchecked cast which is valid");

		}

	}

	/**
	 * Returns this chain represented by an array.
	 * Since the time complexity of this method is <code>O(n)</code>,
	 * it is best to {@link #trim(int, boolean)} this chain if it is large.
	 * @return The chain represented by an array.
	 */
	@SuppressWarnings("unchecked")
	public E[] toArray() {

		var b = toBlockArray();
		var a = new Object[count];

		for(int i = 0; i <= count; i++) {
			HashChainBlock block = b[i];
			var d = block.data;
			a[i] = d;
		}

		return (E[]) a;

	}

	/**
	 * Returns this chain converted to an array in its original form,
	 * except the array capacity is the number of elements in this chain.
	 * This method is not recommended as the returned array may contain null elements.
	 * @return The chain represented by an array.
	 */
	public HashChainBlock[] toBlockArray() {

		return data;

	}

	public boolean isInitWithNullBlock() {
		return initWithNullBlock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashChain<E> trim(int index, boolean reduceCapacity) {

		HashChain<E> h = reduceCapacity
				? new HashChain<>(arrayIncrement, count, false)
				: new HashChain<>(arrayIncrement, data.length, false);
		h.data[0] = this.data[index];
		int e = 1;
		for(int i = index + 1; i < data.length; i++) {
			HashChainBlock datum = data[i];
			if(datum == null && reduceCapacity) {
				break;
			}
			h.data[e] = datum;
			e++;
		}

		return h;

	}
}