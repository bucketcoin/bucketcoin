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

import com.google.common.eventbus.EventBus;
import com.google.common.reflect.ClassPath;
import net.bucketcoin.runtime.exception.InitializationException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.jar.JarFile;

@SuppressWarnings("UnstableApiUsage")
public final class EventCentral {

	private static final EventCentral eventCentral = new EventCentral();
	private static final EventClassLoader eventClassLoader = new EventClassLoader();
	private final HashSet<Class<?>> events = new HashSet<>();
	final HashMap<Class<?>, EventHandler<? extends Event>> eventAssoc = new HashMap<>();

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

	/**
	 * Loads events from a JAR.
	 * @param jarFile The JAR file to scan for events and load them.
	 * @throws IOException if an I/O error happens.
	 * @throws NullPointerException if jarFile is null.
	 */
	public static boolean loadFromJAR(JarFile jarFile) throws IOException {
		ClassPath cp = ClassPath.from(eventClassLoader);
		for(ClassPath.ClassInfo c : cp.getAllClasses()) {
			try {
				var loadedClass = c.load();
				addEventType(loadedClass);
			} catch(IllegalArgumentException illegalArgumentException) {
				return false;
			}
		}
		return true;
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
