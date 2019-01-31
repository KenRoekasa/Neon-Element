package Networking.Test;

import Networking.Server.MultiCastServerThread;

public class BroadCastServer {
	public static void main(String[] args) {
		new MultiCastServerThread().start();
	}
}
