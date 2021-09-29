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

package net.bucketcoin.networking;

import lombok.AccessLevel;
import lombok.Getter;
import net.bucketcoin.runtime.event.Event;
import net.bucketcoin.runtime.event.EventHandler;
import net.bucketcoin.runtime.event.events.HandshakeEvent;
import net.bucketcoin.runtime.exception.InitializationException;
import net.bucketcoin.wallet.Wallet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * This abstract class is meant to help create implementations of a Peer in P2P programming.
 * Following Java conventions, this class should be closed and destroyed upon exit.
 * @see java.io.Closeable
 */
public abstract class AbstractPeer implements Closeable {

	public static final String
								BLOCK = "BLK",
								REQUEST_TRADE = "TRAD",
								TRANSACTION = "TRAN",
								INSERT_PEER = "JOIN",
								LIST_PEER = "LIST",
								PEER_NAME = "NAME",
								QUERY = "QER",
								Q_RESPONSE = "RESP",
								PEER_QUIT = "QUIT",
								REPLY = "REPL",
								ERROR = "ERR";
	private final EventHandler<? extends Event> generalHandler;
	private final Wallet wallet;

	@Getter
	protected ServerSocket serverSocket;
	protected Socket socket;
	/**
	 * The port for the peer. If the port is <code>-1</code>,
	 * it is from an incoming peer, and not on the local node.
	 */
	@Getter
	protected final int port;
	@Getter
	protected final InetAddress localIPAddress = InetAddress.getLocalHost();
	protected final HashSet<? extends AbstractPeer> peers = new HashSet<>();
	protected EventHandler<HandshakeEvent> handshakeHandler;
	protected final HandshakeEvent handshakeEvent;
	/**
	 * The socket of this running machine.
	 */
	@Getter(AccessLevel.PROTECTED)
	private final Socket localSocket = new Socket(InetAddress.getLocalHost(), getPort());

	/**
	 * Creates a new Peer.
	 *
	 * @throws UnknownHostException if the computer does not know the meaning of <code>localhost</code>.
	 *
	 * @param port The port to run on.
	 * @param handshakeHandler The {@link EventHandler} for catching handshakes from other peers. Can be <code>null</code>.
	 * @param generalHandler The general EventHandler for catching events such as {@link net.bucketcoin.runtime.event.events.QueryEvent QueryEvent}, etc.
	 *
	 * @implNote Validation of the parameters is done in this constructor,
	 * where<br><code>!Pattern.compile("[0-9]+").matcher(port).matches() && (port.length() > 4)</code>.
	 * using a {@link net.bucketcoin.runtime.event.events.QueryEvent QueryEvent}. Additionally, parameters
	 * {@link #port}, {@link #generalHandler}, {@link #handshakeHandler} and
	 *
	 * @implSpec The parameter <i>handshakeHandler</i> should be checked for nullity. If <i>handshakeHandler</i> is null,
	 * throw an {@link UnsupportedOperationException} for methods utilizing sockets to send and retrieve {@link Event Events}.
	 * For example, the method {@link #init(ConnectionMode)} (and also {@link #init()} if called) should throw the specified
	 * <code>UnsupportedOperationException</code>, probably with a helpful notice indicating that the {@link #handshakeHandler}
	 * is <code>null</code>. It may be <code>null</code> because an incoming peer (see {@link #AbstractPeer(Socket)}) may have
	 * connected and is waiting to be added to the {@link #peers} {@link HashSet}.
	 */
	public AbstractPeer(@NotNull String port, @Nullable EventHandler<HandshakeEvent> handshakeHandler, @Nullable EventHandler<? extends Event> generalHandler, @NotNull Wallet wallet) throws IOException, IllegalAccessException {

		if(port.length() > 5)
			throw new IllegalArgumentException("Length of string 'port' cannot be more than five digits.");
		if(!Pattern.compile("[0-9]+").matcher(port).matches())
			throw new IllegalArgumentException("String 'port' can only contain digits.");
		int intPort = Integer.parseInt(port);
		if(intPort > 65535)
			throw new IllegalArgumentException("Port cannot exceed 65535");

		try {
			this.getClass().getDeclaredMethod("run");
		} catch(NoSuchMethodException e) {
			var iae = new IllegalAccessException();
			var iae2 = iae.initCause(e);
			throw (IllegalAccessException) iae2;
		}

		this.port = intPort;
		this.handshakeHandler = handshakeHandler;
		this.handshakeEvent = new HandshakeEvent(handshakeHandler);
		this.generalHandler = generalHandler;
		this.wallet = wallet;
	}

	/**
	 * Creates a Peer from the socket. Some operations will not be available. See the implementation requirements
	 * for more information.
	 * @param incomingSocket The incoming peer socket.
	 * @throws UnknownHostException See {@link InetAddress#getLocalHost()} for the fuss.
	 * @see UnknownHostException
	 * @see InetAddress#getLocalHost()
	 * @see #AbstractPeer(String, EventHandler, EventHandler, Wallet)
	 */
	protected AbstractPeer(@NotNull Socket incomingSocket) throws IOException {

		this.socket = incomingSocket;
		port = -1;
		this.handshakeEvent = null;
		this.generalHandler = null;
		this.wallet = Wallet.NullWallet.getWallet();

	}

	/**
	 * Creates a new Peer.
	 * @throws UnknownHostException if the computer does not know the meaning of <code>localhost</code>.
	 * @implNote Validation of the parameters is done in this constructor,
	 * where<br><code>!Pattern.compile("[0-9]+").matcher(port).matches() && (port.length() > 4)</code>.
	 * @apiNote This should NOT be used to create incoming peers, and instead should be accessed by specific event broadcasting
	 * using a {@link net.bucketcoin.runtime.event.events.QueryEvent QueryEvent}.
	 */
	public AbstractPeer(int port, EventHandler<HandshakeEvent> handshakeEventHandler) throws IOException, IllegalAccessException {
		this(String.valueOf(port), handshakeEventHandler, null, Wallet.NullWallet.getWallet());
	}

	/**
	 * Initializes the ServerSocket and sockets.
	 * @throws InitializationException if the initialization fails.
	 * @throws IOException if an I/O error occurs.
	 * @implSpec This method should also concurrently run a <code>while</code>
	 * loop for incoming {@link net.bucketcoin.runtime.event.Event Events} and then <code>return</code>, or
	 * concurrency in Java can be utilized. This could be achieved with
	 * {@link Thread Threads} or coroutine(s/implementations).
	 */
 	public abstract void init(ConnectionMode protocol) throws IOException;

	/**
	 * @throws InitializationException if the initialization fails.
	 * @throws IOException if an I/O error occurs.
	 */
	public final void init() throws IOException {

		this.init(ConnectionMode.TCP);

	}

	/**
	 * Attempts to safely close this node and release any system resources & connections associated
	 * with it. If the peer node is already closed then invoking this
	 * method throws an {@link IllegalStateException}.
	 *
	 * @throws IOException if an I/O error occurs
	 * @throws IllegalStateException if the peer is already closed.
	 */
	@Override
	public abstract void close() throws IOException;

	/**
	 * Broadcasts the event to other peers in the network.
	 * @param event The event to broadcast to the network.
	 * @implSpec The {@link Event} is sent using the socket's {@link java.io.OutputStream}.
	 * Additionally, the {@link #socket} field must not be <code>null</code> before
	 * sending the Event.
	 * @see Event
	 * @see Socket#getOutputStream()
	 */
	protected abstract void broadcast(Socket socket, Event event) throws IOException;

}