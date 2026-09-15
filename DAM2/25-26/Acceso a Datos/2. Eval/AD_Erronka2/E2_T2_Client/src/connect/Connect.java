package connect;

public final class Connect {
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 6000;
	private static final int TIMEOUT = 5000;

	public String getServerHost() {
		return SERVER_HOST;
	}

	public int getServerPort() {
		return SERVER_PORT;
	}

	public int getTimeout() {
		return TIMEOUT;
	}
}