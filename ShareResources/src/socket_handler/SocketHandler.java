package socket_handler;

import chain.Chain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.SocketException;

public abstract class SocketHandler implements Runnable {
    protected final SocketWrapper socket;

    public SocketHandler(SocketWrapper socket) {
        this.socket = socket;
    }

    public void run() {
        // resolve the authentication
        boolean authenticate;
        try {
            authenticate = handleAuthentication();
        } catch (Exception e) {
            e.printStackTrace();
            authenticate = false;
            try {
                socket.write("{error: \"" + e.getMessage() + "\"}");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // loop to read message from the socket
        while (authenticate) {
            // read a message from the socket
            String requestString;
            try {
                requestString = socket.read();
            } catch (SSLException | SocketException e) {
                System.err.println("This error needed to be named");
                e.printStackTrace();
                break;
            } catch (IOException e) {
                System.err.println("Fail to read message from socket");
                e.printStackTrace();
                continue;
            }
            // if a request is null meaning that the other side of the socket is closed
            if (requestString == null) break;
            // let a thread handle the request
            new Thread(() -> {
                // convert to json object
                Gson gson = new Gson(); // new gson object for each thread because sharing gson on multiple threads risk processing one object while another one wrest control and use it
                JsonObject request = gson.fromJson(requestString, JsonObject.class);
                // if the request cannot be handled successfully, execute the cleanup chain
                if (!handleRequest(request).execute()) handleFailure(request).execute();
            }).start();
        }
        // close the socket after done
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cleanup();
    }

    public abstract Chain handleRequest(JsonObject request);

    public abstract Chain handleFailure(JsonObject request);

    public abstract boolean handleAuthentication();

    public abstract void cleanup();
}
