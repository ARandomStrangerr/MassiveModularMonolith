package memorable;

import socket_handler.connection_manager.ListenerHandler;
import socket_handler.ListenerWrapper;
import socket_handler.SocketWrapper;

public class ConnectionManager {
    public static SocketWrapper socketToDataStream;
    public static ListenerWrapper listener;
    public static database.ConnectionManager connectionPool;
}
