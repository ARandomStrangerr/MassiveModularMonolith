package socket_handler.gmail;

import socket_handler.ListenerWrapper;
import socket_handler.SocketWrapper;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class OauthListenerManager {
    private final LinkedList<ListenerWrapper> availableListener;
    private final LinkedList<OauthListenerManager> queue;
    private final Hashtable<Integer, ListenerWrapper> reservedListener;

    public OauthListenerManager(int[] ports) throws IOException {
        reservedListener = new Hashtable<>();
        queue = new LinkedList<>();
        availableListener = new LinkedList<>();
        for (int port : ports) {
            ListenerWrapper listener = new ListenerWrapper(port);
            availableListener.add(listener);
        }
    }

    /**
     * reserve a listener.
     * this listener will later on be used to accept a socket then read the oauth info that is sent back by the Google oauth.
     * @return integer which represents the port which is
     * @throws NoSuchElementException
     * @throws InterruptedException
     */
    public synchronized int reserveListener() throws NoSuchElementException, InterruptedException {
        ListenerWrapper listener;
        if (availableListener.isEmpty() || !queue.isEmpty()) {
            queue.add(this);
            wait();
        }
        listener = availableListener.removeFirst();
        int port = listener.getPort();
        reservedListener.put(port, listener);
        return port;
    }

    public String getData(int port) throws NoSuchElementException, IOException {
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
