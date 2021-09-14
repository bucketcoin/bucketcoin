package net.bucketcoin.runtime.event;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.stream.Stream;

public class EventClassLoader extends ClassLoader {

	protected EventClassLoader() {
		super();
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return loadEventClass(name);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated <b><p><font color="red">DO NOT USE THIS METHOD!</font></p></b> The boolean parameter "resolve"
	 * is not regarded by the EventClassLoader, so running this method is equivalent to {@linkplain EventClassLoader#loadEventClass(String)}.
	 */
	@Override
	@Deprecated
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		return loadEventClass(name);
	}

	/**
	 * Loads a class extending an {@link Event}.
	 * @param name The binary name of the class to load.
	 * @throws ClassNotFoundException if the class could not be found.
	 * @throws IllegalArgumentException if the class does not inherit (either directly or indirectly) the {@link Event} class.
	 */
	protected Class<?> loadEventClass(String name) throws ClassNotFoundException, IllegalArgumentException {

		synchronized(getClassLoadingLock(name)) {
			var c = findLoadedClass(name);
			if(c == null) {
				c = getParent().loadClass(name);
				if(!Event.class.isAssignableFrom(c)) throw new IllegalArgumentException("Class " + name + " does not inherit from Event");
				if(Event.class.isAssignableFrom(c)) {
					EventCentral.addEventType(c);
				}
			}
			resolveClass(c);
			return c;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getClassLoadingLock(String className) {
		return super.getClassLoadingLock(className);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> findClass(String moduleName, String name) {
		return super.findClass(moduleName, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected URL findResource(String moduleName, String name) throws IOException {
		return super.findResource(moduleName, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	@Override
	public URL getResource(String name) {
		return super.getResource(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return super.getResources(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<URL> resources(String name) {
		return super.resources(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected URL findResource(String name) {
		return super.findResource(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		return super.findResources(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	@Override
	public InputStream getResourceAsStream(String name) {
		return super.getResourceAsStream(name);
	}
}