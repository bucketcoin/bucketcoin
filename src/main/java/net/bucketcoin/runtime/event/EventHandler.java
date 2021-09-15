package net.bucketcoin.runtime.event;

/**
 * Represents an Event Handler in event-driven programming.
 * @param <T> The {@link net.bucketcoin.runtime.event.Event} that this instance of an EventHandler is targeted
 *           to handle.
 */
public interface EventHandler<T extends Event> {

	void handle(T event);

}
