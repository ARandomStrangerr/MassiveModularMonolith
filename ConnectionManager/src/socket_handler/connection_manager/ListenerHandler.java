package socket_handler.connection_manager;

import chain.Chain;
import chain.connection_manager.request_handler.ChainProcessRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;
import socket_handler.ListenerWrapper;
import socket_handler.SocketHandler;
import socket_handler.SocketWrapper;

import java.io.IOException;

public class ListenerHandler extends socket_handler.ListenerHandler {
    private final int timeout;

    public ListenerHandler(ListenerWrapper listener, int timeout) {
        super(listener);
        this.timeout = timeout;
    }

    @Override
    public SocketHandler socketHandler(SocketWrapper socket) {
        return new SocketHandler(socket) {
            @Override
            public Chain handleRequest(JsonObject request) {
                return new ChainProcessRequest(request, socket.getName());
            }

            @Override
            public Chain handleFailure(JsonObject request) {
                return null;
            }

            @Override
            public boolean handleAuthentication() {
                // set timeout for the chain
                try {
                    socket.setTimeout(timeout);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                // read the data from the socket
                String authenticationString;
                try {
                    authenticationString = socket.read();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                // reset timeout
                try {
                    socket.setTimeout(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                // convert the string into json object
                JsonObject authenticationJson = new Gson().fromJson(authenticationString, JsonObject.class);
                // check if there is already socket under this nameConnectionManager.getInstance().listenerWrapper.getSocket(socket.getName())
                if (ConnectionManager.getInstance().listenerWrapper.getSocket(authenticationJson.get("macAddress").getAsString()) != null) {
                    try {
                        socket.write("{error: \"Có kết nối khác đã được thiết lập với máy chủ\"}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                // set the name of the socket
                socket.setName(authenticationJson.get("macAddress").getAsString());
                // put socket into the storage
                ConnectionManager.getInstance().listenerWrapper.putSocket(socket.getName(), socket);
                // print out message
                System.out.printf("Client with mac address %s has connected to the network\n", socket.getName());
                return true;
            }

            public void cleanup() {
                if (socket.getName() != null) {
                    ConnectionManager.getInstance().listenerWrapper.removeSocket(socket.getName());
                    System.out.printf("Client with mac address %s has disconnected\n", socket.getName());
                }
            }
        };
    }
}
