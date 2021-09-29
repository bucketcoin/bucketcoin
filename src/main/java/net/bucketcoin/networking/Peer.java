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

import lombok.Getter;
import lombok.SneakyThrows;
import net.bucketcoin.runtime.event.Event;
import net.bucketcoin.runtime.event.EventHandler;
import net.bucketcoin.runtime.event.events.HandshakeEvent;
import net.bucketcoin.runtime.exception.InitializationException;
import net.bucketcoin.util.SerializationResources;
import net.bucketcoin.wallet.Wallet;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Peer extends AbstractPeer {

	@Getter
	private ServerSocket serverSocket;
	private @Nullable Socket socket;
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private @NotNull State peerOperativeState = State.CLOSED;

	/**
	 * Creates a new Peer.
	 *
	 * @throws UnknownHostException if the computer does not know the meaning of <code>localhost</code>.
	 *
	 * @param port The port to run on.
	 * @param handshakeEventHandler The {@link EventHandler} for catching handshakes from other peers. Can be <code>null</code>.
	 * @param generalHandler The general EventHandler for catching events such as {@link net.bucketcoin.runtime.event.events.QueryEvent QueryEvent}, etc.
	 */
	public Peer(String port, EventHandler<HandshakeEvent> handshakeEventHandler, EventHandler<? extends Event> generalHandler, @NotNull Wallet wallet) throws IOException, IllegalAccessException {
		super(port, handshakeEventHandler, generalHandler, wallet);
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
	@SuppressWarnings("InfiniteLoopStatement")
	public void init(ConnectionMode protocol) throws IOException {

		serverSocket = new ServerSocket(getPort());
		peerOperativeState = State.OPERATING;
		executorService.submit(() -> {

			try {

				socket = serverSocket.accept();
				broadcast(socket, handshakeEvent);

				while(true) {
					executorService.submit(() -> {

					});
				}

			} catch(IOException e) {
				try {
					close();
				} catch(IOException e2) {
					throw new IOError(e2);
				}
				throw new RuntimeException(e);
			}

		});

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {

		if(peerOperativeState == State.CLOSED) throw new IllegalStateException("Peer already closed");

		peerOperativeState = State.CLOSING;

		if(socket != null) {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		}
		serverSocket.close();
		executorService.shutdown();

		peerOperativeState = State.CLOSED;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void broadcast(@NotNull Socket socket, @NotNull Event event) throws IOException {
		var outputStream = socket.getOutputStream();
		SerializationUtils.serialize(event, outputStream);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////                           INNER CLASSES                           //////
	///////////////////////////////////////////////////////////////////////////////

	private enum State {
		OPERATING,
		CLOSING,
		CLOSED
	}

	private static final class PeerHandshakeThread extends Thread {

		private final Socket clientSocket;
		private PrintWriter out;
		private BufferedReader in;

		public PeerHandshakeThread(Socket socket) {
			this.clientSocket = socket;
		}

		@SneakyThrows
		public void run() {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (".".equals(inputLine)) {
					out.println("bye");
					break;
				}
				out.println(inputLine);
			}

			in.close();
			out.close();
			clientSocket.close();
		}

	}

}
