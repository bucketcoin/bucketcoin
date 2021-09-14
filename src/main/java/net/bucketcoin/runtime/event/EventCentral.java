package net.bucketcoin.runtime.event;

import com.google.common.eventbus.EventBus;
import net.bucketcoin.runtime.exception.InitializationException;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

@SuppressWarnings("UnstableApiUsage")
public final class EventCentral {

	public static final EventBus eventBus = new EventBus();
	private static final EventCentral eventCentral = new EventCentral();
	private static final EventClassLoader eventClassLoader = new EventClassLoader();
	private final HashSet<Class<?>> events = new HashSet<>();

	public static EventCentral getInstance() {
		return eventCentral;
	}

	private EventCentral() {
		if(!init()) {
			throw new InitializationException();
		}
	}

	static void addEventType(Class<?> event) {
		if(!Event.class.isAssignableFrom(event)) throw new IllegalArgumentException("Class " + event.getTypeName() + " does not inherit Event");
		eventCentral.events.add(event);
	}

	public static <T extends Event> void addEventType(@NotNull T event) {
		addEventType(event.getClass());
	}

	private static HashSet<Class<?>> getEventSet() {
		return eventCentral.events;
	}

	private static boolean init() {

		try {

			for(Class<?> clazz : getEventSet()) {
				eventClassLoader.loadEventClass(clazz.getName());
			}

			return true;

		} catch(Exception e) {
			return false;
		}

	}

}
