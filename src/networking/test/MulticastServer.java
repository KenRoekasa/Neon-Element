package networking.test;

public class MulticastServer {
	public static void main(String[] args) throws java.io.IOException {
    	System.setProperty("java.net.preferIPv4Stack", "true");

        new MulticastServerThread().start();
    }
}
