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

package net.bucketcoin.runtime.event;

/**
 * Represents an Event Handler in event-driven programming.
 * @param <T> The {@link net.bucketcoin.runtime.event.Event} that this instance of an EventHandler is targeted
 *           to handle.
 */
public interface EventHandler<T extends Event> {

	void handle(Event event);

}
