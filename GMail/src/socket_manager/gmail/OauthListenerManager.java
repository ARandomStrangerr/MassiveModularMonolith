package socket_manager.gmail;

import socket_handler.ListenerWrapper;
import socket_handler.SocketWrapper;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class OauthListenerManager {
    private final LinkedList<ListenerWrapper> availableListener;
    private final Hashtable<Integer, ListenerWrapper> reservedListener;

    public OauthListenerManager(int[] ports) throws IOException {
        reservedListener = new Hashtable<>();
        availableListener = new LinkedList<>();
        for (int port : ports) {
            ListenerWrapper listener = new ListenerWrapper(port);
            availableListener.add(listener);
        }
    }

    public int reserveListener() throws NoSuchElementException {
        ListenerWrapper listener;
        synchronized (availableListener) {
            listener = availableListener.removeFirst();
        }
        int port = listener.getPort();
        reservedListener.put(port, listener);
        return port;
    }

    public String getData(int port) throws NoSuchElementException, IOException{
        ListenerWrapper listener = reservedListener.remove(port);
        SocketWrapper socket = listener.accept();
        String data = socket.read();
        socket.close();
        synchronized (availableListener) {
            availableListener.add(listener);
        }
        return data;
    }
}
