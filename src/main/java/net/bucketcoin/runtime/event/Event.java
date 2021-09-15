package net.bucketcoin.runtime.event;

import lombok.Getter;


public abstract class Event {

	@Getter
	private final Object[] args;

	public Event(Object... args) {
		this.args = args;
		EventCentral.addEventType(this);
	}

}