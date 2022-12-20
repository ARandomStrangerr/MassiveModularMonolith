package memorable;

import socket_handler.connection_manager.ListenerHandler;
import socket_handler.ListenerWrapper;
import socket_handler.SocketWrapper;

public class ConnectionManager {
    public String moduleName;
    public SocketWrapper socket;
    public ListenerWrapper listenerWrapper;
    public database.ConnectionManager database;
    public ListenerHandler listenerHandler;

    private static ConnectionManager instance;

    public static ConnectionManager getInstance() {
        if (instance == null) instance = new ConnectionManager();
        return instance;
    }
}
