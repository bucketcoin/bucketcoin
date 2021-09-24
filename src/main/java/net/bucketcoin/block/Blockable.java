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

package net.bucketcoin.block;

import java.io.Serializable;

/**
 *  An interface to mark objects that can be put in blocks.
 */
public interface Blockable extends Serializable {

    /**
     * Returns the gas fee in 0.000001 BCKT.
     */
    double getGasFee();

    /**
     * Converts the {@linkplain Blockable} into a JSON string.
     */
    String toString();

}
