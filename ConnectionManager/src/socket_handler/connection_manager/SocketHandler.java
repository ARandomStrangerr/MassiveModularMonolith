package socket_handler.connection_manager;

import chain.Chain;
import chain.connection_manager.request_handler.ChainProcessReturnRequest;
import memorable.ConnectionManager;
import socket_handler.SocketWrapper;
import com.google.gson.JsonObject;

import java.io.IOException;

public class SocketHandler extends socket_handler.SocketHandler {
    public SocketHandler(SocketWrapper socket) {
        super(socket);
    }

    @Override
    public Chain handleRequest(JsonObject request) {
        return new ChainProcessReturnRequest(request);
    }

    @Override
    public Chain handleFailure(JsonObject request) {
        return null;
    }

    @Override
    public boolean handleAuthentication() {
        // write the name of this module to the DataStream module
        try {
            socket.write("{moduleName:\"" + ConnectionManager.getInstance().moduleName + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        socket.setName("DataStream");
        return true;
    }

    public void cleanup(){
        System.exit(1);
    }
}
