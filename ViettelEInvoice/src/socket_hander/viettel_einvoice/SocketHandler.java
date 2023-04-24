package socket_hander.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.io.IOException;

public class SocketHandler extends socket_handler.SocketHandler {
    private final String moduleName;
    public SocketHandler(SocketWrapper socket, String moduleName) {
        super(socket);
        this.moduleName = moduleName;
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
            socket.write("{moduleName:\""+ moduleName +"\"}");
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        socket.setName("DataStream");
        return true;
    }

    @Override
    public void cleanup() {
        System.exit(1);
    }
}
