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

package net.bucketcoin.exception;

/**
 * This class represents an exception thrown due to {@link net.bucketcoin.contract} operations.
 */
public class ContractException extends Exception {

	public ContractException(String message) {
		super(message);
	}

	public ContractException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContractException(Throwable cause) {
		super(cause);
	}

	public ContractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
