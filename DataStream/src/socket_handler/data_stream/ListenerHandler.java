package socket_handler.data_stream;

import chain.Chain;
import chain.data_stream.handle_failure.ChainHandleFailure;
import chain.data_stream.handle_request.ChainHandleRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import memorable.DataStream;
import socket_handler.ListenerWrapper;
import socket_handler.SocketHandler;
import socket_handler.SocketWrapper;

import javax.net.ssl.SSLException;
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
                return new ChainHandleRequest(request, socket.getName());
            }

            @Override
            public Chain handleFailure(JsonObject request) {
                return new ChainHandleFailure(request);
            }

            @Override
            public boolean handleAuthentication() {
                // set the timeout for the socket
                try {
                    socket.setTimeout(timeout);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                // read from the socket
                String authenticateString;
                try {
                    authenticateString = socket.read();
                } catch (SSLException e) {
                    System.err.println("The incoming socket has invalid credential");
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    System.err.println("Cannot read from socket");
                    e.printStackTrace();
                    return false;
                }
                // convert the message into json format
                JsonObject authenticateJson;
                try {
                    authenticateJson = new Gson().fromJson(authenticateString, JsonObject.class);
                } catch (Exception e) {
                    System.err.println("Incorrect Json format");
                    e.printStackTrace();
                    return false;
                }
                // remove the timeout for the socket
                try {
                    socket.setTimeout(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                // put the socket into storage under given name
                DataStream.getInstance().getListener().putSocket(authenticateJson.get("moduleName").getAsString(), socket);
                // set the name for the socket
                socket.setName(authenticateJson.get("moduleName").getAsString());
                // print out the socket accepted
                System.out.printf("Accepted a socket from module with name of %s\n",socket.getName());
                return true;
            }

            public void cleanup(){
                if (socket.getName() != null) {
                    System.out.printf("Socket from module with name of %s disconnected\n",socket.getName());
                    DataStream.getInstance().getListener().removeSocket(socket.getName());
                }
            }
        };
    }
}