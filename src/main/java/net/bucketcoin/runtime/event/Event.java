package net.bucketcoin.runtime.event;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * This class represents an event that can be caught by an associated {@link EventHandler}.
 */
public abstract class Event {

	@Getter
	private final Object[] args;

	/**
	 *
	 * @param associatedHandler The {@link EventHandler} to associate the handling of this event with.
	 * @param args The arguments passed to the firing of the event.
	 */
	public Event(EventHandler<? extends Event> associatedHandler, Object... args) {
		this.args = args;
		init(associatedHandler);
	}

	/**
	 *
	 * @throws IllegalCallerException if the class that is calling this method is <br>
	 * 1. A nested class inside this {@link Event} implementation.
	 * 2. The {@link Event} implementation itself.
	 */
	@SneakyThrows			   /*  Cannot be called by itself  */
	public final void fire() throws IllegalCallerException {
		var stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		var executor = stackWalker.getCallerClass();
		if(executor.getNestHost() == this.getClass()
		|| executor.equals(getClass())) {
			throw new IllegalCallerException();
		}
	}

	/**
	 * Prevent removal of calling necessary methods in the constructor.
	 */
	private void init(EventHandler<? extends Event> eventHandler) {
		EventCentral.addEventType(this);
		var m = EventCentral.getInstance().eventAssoc;
		m.put(eventHandler, this.getClass());
	}

}