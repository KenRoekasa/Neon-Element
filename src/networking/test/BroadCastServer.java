package networking.test;

import networking.server.MultiCastServerThread;

public class BroadCastServer {
	public static void main(String[] args) {
		new MultiCastServerThread().start();
	}
}
