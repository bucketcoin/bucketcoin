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

public interface Trimmable<E> {

	/**
	 * Trims the object to a shorter size, best used
	 * to keep a small storage. The implementor is
	 * trimmed to the specified index.
	 * This method does two things
	 * to the implementor:<br><br>
	 *
	 * 1. Cut the size of the implementor, for example
	 * a full list of capacity 20 gets cut from index of 4, so the chain
	 * now has 15 elements remaining. <br>
	 * 1.5. If the parameter <code>reduceCapacity</code> is <code>true</code>,
	 * the new size of the implementor is reduced to the specified capacity.
	 * <br>
	 * 2. Changes the backing storage (for example, an array) and returns the trimmed implementor.
	 *
	 * @param index The index to trim off. The index specified will be included in the trimmed implementor.
	 *
	 * @return The trimmed object.
	 */
	E trim(int index, boolean reduceCapacity);

}
