package com.superschach.superschach.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

public class Warteliste {

	private Server server;
	private HashMap<InetAddress, LinkedList<SocketCollector>> waiting = new HashMap<InetAddress, LinkedList<SocketCollector>>();
	AtomicLong token = new AtomicLong();

	public Warteliste(Server server) {
		this.server = server;
	}

	private LinkedList<SocketCollector> getSocketCollectorList(InetAddress ip) {
		LinkedList<SocketCollector> scl = null;
		synchronized (waiting) {
			scl = waiting.get(ip);
			if (scl == null) {
				scl = new LinkedList<SocketCollector>();
			}
			waiting.put(ip, scl);
		}
		return scl;
	}

	private static SocketCollector getSocketCollector(
			LinkedList<SocketCollector> scl, long token) {
		synchronized (scl) {
			for (SocketCollector sc : scl) {
				if (sc.getToken() == token)
					return sc;
			}
			SocketCollector sc = new SocketCollector(token);
			scl.add(sc);
			return sc;
		}
	}

	private SocketCollector getSocketCollector(InetAddress ip, long token) {
		LinkedList<SocketCollector> scl = getSocketCollectorList(ip);
		return getSocketCollector(scl, token);

	}

	private void removeSocketCollector(InetAddress ip, long token) {
		LinkedList<SocketCollector> scl = getSocketCollectorList(ip);
		synchronized (scl) {
			for (int i = 0; i < scl.size(); i++) {
				if(scl.get(i).getToken()==token)
				{
					scl.remove(i);
				}
			}
		}

	}

	public void addSocket(final Socket socket) throws IOException {

		long token;
		if (socket.getLocalPort() == Server.getBasePort()[0]) {
			token = this.token.incrementAndGet();
			byte[] buff = toArray(token);
			socket.getOutputStream().write(buff);
		} else {
			byte[] buff = new byte[8];
			InputStream is=socket.getInputStream();
			is.read(buff);
			token = toLong(buff);
		}

		final SocketCollector sc = getSocketCollector(socket.getInetAddress(), token);
		sc.addSocket(socket);
		if (!sc.isReady()) {
			return;
		}

		removeSocketCollector(socket.getInetAddress(),token);
		new Thread() {
			public void run() {
				server.neuerClient(sc);
			}
		}.run();
	}

	public static byte[] toArray(long l) {
		byte[] ret = new byte[8];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (l >> i * 8);
		}
		return ret;
	}

	public static long toLong(byte[] b) {
		long ret = 0;
		for (int i = 7; i >= 0; i--) {
			ret = (ret << 8) + b[i];
		}
		return ret;
	}
	
	public static void main(String[] args)
	{
		byte[] test={1,2,3,4};
		System.out.println(Long.toBinaryString(toLong(test)));
	}
}
