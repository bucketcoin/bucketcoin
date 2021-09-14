package net.bucketcoin.runtime.event;

import com.google.common.eventbus.Subscribe;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("UnstableApiUsage")
public abstract class Event {

	@Contract("null -> fail")
	@Subscribe
	public abstract <T extends Event> void handle(T event);

	public Event() {
		EventCentral.addEventType(this);
	}

}