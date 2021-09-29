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

package net.bucketcoin.runtime.event.events;

import net.bucketcoin.runtime.event.Event;
import net.bucketcoin.runtime.event.EventHandler;
import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HandshakeEvent extends Event {

	private final Wallet wallet;

	/**
	 * @param associatedHandler The {@link EventHandler} to associate the handling of this event with.
	 * @param args              The wallet of the user.
	 */
	public HandshakeEvent(EventHandler<? extends Event> associatedHandler, Object... args) {

		super(associatedHandler, args);
		wallet = (Wallet) Objects.requireNonNull(args)[0];

	}
}
