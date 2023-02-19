package memorable;

import socket_handler.SocketWrapper;

public class GMail {
    private static GMail instance;

    public static GMail getInstance() {
        if (instance == null) instance = new GMail();
        return instance;
    }

    public String clientId;
    public String clientSecret;
    public String redirectUri;
    public String moduleName;
    public SocketWrapper socketToDataStream;
}
