package net.bucketcoin.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is similar to a {@link java.util.Deque}, however with a FIFO approach.
 * @param <E> The element to store.
 */
public interface Chain<E> extends Collection<E> {
	/**
	 * Retrieves and removes the first element of this chain.  This method
	 * differs from {@link #pollFirst pollFirst} only in that it throws an
	 * exception if this chain is empty.
	 *
	 * @return the head of this chain
	 * @throws NoSuchElementException if this chain is empty
	 */
	
	E removeFirst();

	/**
	 * Retrieves and removes the first element of this chain,
	 * or returns {@code null} if this chain is empty.
	 *
	 * @return the head of this chain, or {@code null} if this chain is empty
	 */
	
	E pollFirst();

	/**
	 * Retrieves, but does not remove, the first element of this chain,
	 * or returns {@code null} if this chain is empty.
	 *
	 * @return the head of this chain, or {@code null} if this chain is empty
	 */
	
	E peekFirst();

	/**
	 * Retrieves, but does not remove, the first element of this chain.
	 * <p>
	 * This method differs from {@link #peekFirst peekFirst} only in that it
	 * throws an exception if this chain is empty.
	 *
	 * @return the head of this chain
	 * @throws NoSuchElementException if this chain is empty
	 */
	
	E getFirst();

	/**
	 * Inserts the specified element at the front of this chain.
	 *
	 * @param e the element to add
	 * @throws ClassCastException       if the class of the specified element
	 *                                  prevents it from being added to this chain
	 * @throws NullPointerException     if the specified element is null and this
	 *                                  chain does not permit null elements
	 * @throws IllegalArgumentException if some property of the specified
	 *                                  element prevents it from being added to this chain
	 */
	
	void addFirst(E e);

	/**
	 * Inserts the specified element at the top of the chain.
	 *
	 * @param e the element to add
	 * @return {@code true} (as specified by {@link Collection#add})
	 * @throws ClassCastException       if the class of the specified element
	 *                                  prevents it from being added to this chain
	 * @throws NullPointerException     if the specified element is null and this
	 *                                  chain does not permit null elements
	 * @throws IllegalArgumentException if some property of the specified
	 *                                  element prevents it from being added to this chain
	 */
	
	boolean add(E e);

	/**
	 * Removes the first occurrence of the specified element from this chain.
	 * If the chain does not contain the element, it is unchanged.
	 * More formally, removes the first element {@code e} such that
	 * {@code Objects.equals(o, e)} (if such an element exists).
	 * Returns {@code true} if this chain contained the specified element
	 * (or equivalently, if this chain changed as a result of the call).
	 *
	 * @param o element to be removed from this chain, if present
	 * @return {@code true} if an element was removed as a result of this call
	 * @throws ClassCastException   if the class of the specified element
	 *                              is incompatible with this chain
	 *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified element is null and this
	 *                              chain does not permit null elements
	 *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
	 */
	
	boolean remove(Object o);

	/**
	 * Returns {@code true} if this chain contains the specified element.
	 * More formally, returns {@code true} if and only if this chain contains
	 * at least one element {@code e} such that {@code Objects.equals(o, e)}.
	 *
	 * @param o element whose presence in this chain is to be tested
	 * @return {@code true} if this chain contains the specified element
	 * @throws ClassCastException   if the class of the specified element
	 *                              is incompatible with this chain
	 *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified element is null and this
	 *                              chain does not permit null elements
	 *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
	 */
	
	boolean contains(Object o);

	/**
	 * Returns the number of elements in this chain.
	 *
	 * @return the number of elements in this chain
	 */
	
	int size();

	/**
	 * Returns an iterator over the elements in this chain in proper sequence.
	 * The elements will be returned in order from first (head) to last (tail).
	 *
	 * @return an iterator over the elements in this chain in proper sequence
	 */
	
	Iterator<E> iterator();
}
