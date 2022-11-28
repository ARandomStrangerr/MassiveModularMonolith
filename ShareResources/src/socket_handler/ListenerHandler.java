package socket_handler;

import java.io.IOException;

public abstract class ListenerHandler implements Runnable {
    private final ListenerWrapper listener;

    public ListenerHandler(ListenerWrapper listener) {
        this.listener = listener;
    }

    public void run() {
        // while true loop to accept socket
        while (true) {
            // accept socket
            SocketWrapper socket;
            try {
                socket = listener.accept();
            } catch (IOException e) {
                System.err.println("Fail to accept a socket");
                e.printStackTrace();
                continue;
            }
            // start a new thread to handle the socket
            new Thread(socketHandler(socket)).start();
        }
    }

    public abstract SocketHandler socketHandler(SocketWrapper socket);
}
