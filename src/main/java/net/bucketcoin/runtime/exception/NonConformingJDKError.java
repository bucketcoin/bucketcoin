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

package net.bucketcoin.runtime.exception;

/**
 * This error is thrown when the JDK does not conform to the Bucketcoin protocol,
 * especially <i>Article 1 of the Bucketcoin Protocol (Annex A)</i> where the JDK must
 * have an "unlimited" security policy for the JCE.
 */
public class NonConformingJDKError extends Error {

	public NonConformingJDKError() {
		super();
	}

	public NonConformingJDKError(String message) {
		super(message);
	}

	public NonConformingJDKError(String message, Throwable cause) {
		super(message, cause);
	}


	public NonConformingJDKError(Throwable cause) {
		super(cause);
	}

	protected NonConformingJDKError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
