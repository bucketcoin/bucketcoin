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

package net.bucketcoin.contract;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

public final class AcceptedContract {

	private AcceptedContract(Contract contract) throws NoSuchMethodException {
		this.contract = contract;
	}

	private Contract contract;
	@SuppressWarnings("ConstantConditions")
	private @NotNull final Method executionMethod = Objects.requireNonNull(contract).getClass().getMethod("execute");

	@SneakyThrows
	public static AcceptedContract fromContract(@NotNull Contract contract) {
		return new AcceptedContract(contract);
	}

}
