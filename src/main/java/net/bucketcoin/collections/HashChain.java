package net.bucketcoin.collections;

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
 * This class implements {@link Chain}, which uses the FIFO approach.
 * @param <E> The type to store.
 */
@SuppressWarnings("ConstantConditions")
public class HashChain<E> extends AbstractCollection<E> implements Chain<E>,
																   Cloneable,
																   Serializable,
																   RandomAccess {

	private int modded = 0;
	private final int arrayIncrement;
	private Object[] data;
	private int count = 0;

	/**
	 * Constructs a HashChain with a capacity of 50 elements and the specified increment.
	 * @param increment How much the backing array used by this class
	 *                  should be increased in the event of no available
	 *                  slots to place an added element into.
	 */
	public HashChain(int increment) {
		this(increment, 50);
	}

	/**
	 * Constructs a HashChain with the specified capacity and increment.
	 * @param increment How much the backing array used by this class
	 *					should be increased in the event of no available
	 * 					slots to place an added element into.
	 *
	 * @param capacity How many elements that the backing array can store.
	 */
	public HashChain(int increment, int capacity) {

		this.arrayIncrement = increment;
		this.data = new HashChainBlock[capacity];

	}

	/**
	 * Constructs a HashChain with a capacity of 50 elements and an increment of 1.
	 */
	public HashChain() {
		this(1, 50);
	}

	/**
	 * Returns an iterator over the elements contained in this collection.
	 *
	 * @return an iterator over the elements contained in this collection
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		return (Iterator<E>) Arrays.stream(data).iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFirst(E e) {

		var b = toBlock(e);
		add(b, data, data.length);

	}

	private void add(HashChainBlock<E> e, Object @NotNull [] eData, int i) {

		if(i == eData.length) grow();
		data[i + 1] = e;
		count = i + 1;

	}

	@Contract("_ -> new")
	private @NotNull HashChainBlock<E> toBlock(E e) {

		return new HashChainBlock<>(getPreviousBlock(), e);

	}

	@SuppressWarnings("unchecked")
	private HashChainBlock<E> getPreviousBlock() {

		return (HashChainBlock<E>) data[count - 1];

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		try {
			var k = getIndex((E) o);
			var willThrowExceptionIfNotContained = k < 0; // confirmed to be false, try to cause ClassCastException
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
	@SuppressWarnings("unchecked")
	public final boolean validate() {

		for(Object o : data) {

			HashChainBlock<E> datum = (HashChainBlock<E>) o;
			int index = getIndex(datum.data);

			if(!datum.inheritsHashFromNullBlock()) { // ignore 2nd prevHash if it inherits a hash from a NullHashChainBlock.
				if(!datum.getPrevHash2().equals(
						((HashChainBlock<E>) data[index - 2]).getHash()
				)
				||
				!datum.getPrevHash2().equals(
						((HashChainBlock<E>) data[index - 1]).getPrevHash()
				)
				) return false;
			}

			if(!datum.getPrevHash().equals(
					((HashChainBlock<E>) data[index - 1]).getHash()
			)) return false;

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
	@SuppressWarnings("unchecked")
	public int getIndex(@NotNull E e) throws NoSuchElementException {

		int k = 0;

		for(Object o : data) {
			var o2 = (HashChainBlock<E>) o;
			if(o2.data == e) return k;
			k++;
		}
		return -1;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E removeFirst() {

		if(count == 0) throw new IllegalStateException();
		final var c = count;
		var e = data[c];
		data[c] = null;
		count--;
		return ((HashChainBlock<E>) e).data;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E pollFirst() {
		try {
			return removeFirst();
		} catch(IllegalStateException i) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E getFirst() {
		if(count == 0) throw new IllegalStateException();
		return ((HashChainBlock<E>) data[count]).data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

		Object[] blocks;
		blocks = new Object[data.length + arrayIncrement];
		System.arraycopy(data, 0, blocks, 0, data.length);
		this.data = blocks;
		modded++;

	}

	@Override
	public int size() {
		return count;
	}

	@SuppressWarnings({"MethodDoesntCallSuperMethod", "unchecked"})
	public HashChain<E> clone() {

		try {

			var clone = new HashChain<>();
			Object[] blocks;
			blocks = clone.data;
			System.arraycopy(data, 0, blocks, 0, data.length);
			clone.modded = 0;
			return (HashChain<E>) clone;

		} catch(ClassCastException ignore) {

			throw new InternalError("Failed to perform an unchecked cast which is valid");

		}

	}
}