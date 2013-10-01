package com.paraschool.editor.server;

import org.apache.http.conn.ClientConnectionManager;

public class ConnectionMonitorThread extends Thread {

	private final ClientConnectionManager connMgr;
	private final int timeout;
	private volatile boolean shutdown;

	public ConnectionMonitorThread(ClientConnectionManager connMgr, int timeout) {
		super();
		this.connMgr = connMgr;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				wait(timeout);
				if(!shutdown)
					connMgr.shutdown();
			}
		} catch (InterruptedException ex) {}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}