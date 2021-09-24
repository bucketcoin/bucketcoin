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

import net.bucketcoin.block.Blockable;

public interface IBlock {

	/**
	 * Retrieves the hash of the {@link IBlock} in a hexadecimal string.
	 * @return The hash in a hexadecimal string, such as <code>0x...</code>
	 */
	String getHash();


	/**
	 * Converts the {@linkplain Blockable} into a JSON string.
	 */
	String toString();


}
