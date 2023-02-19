package socket_manager.gmail;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.GMail;
import socket_handler.SocketWrapper;

import java.io.IOException;

public class SocketHandler extends socket_handler.SocketHandler {
    public SocketHandler(SocketWrapper socket) {
        super(socket);
    }

    @Override
    public Chain handleRequest(JsonObject request) {
        return null;
    }

    @Override
    public Chain handleFailure(JsonObject request) {
        return null;
    }

    @Override
    public boolean handleAuthentication() {
        try {
            socket.write(String.format("{\"moduleName\":\"%s\"}", GMail.getInstance().moduleName));
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        socket.setName("DataStream");
        return true;
    }

    @Override
    public void cleanup() {

    }
}
