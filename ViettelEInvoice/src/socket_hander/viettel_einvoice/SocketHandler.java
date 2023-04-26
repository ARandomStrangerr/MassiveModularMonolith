package socket_hander.viettel_einvoice;

import chain.Chain;
import chain.viettel_einvoice.ChainHandleFailure;
import chain.viettel_einvoice.ChainHandleRequest;
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
        return new ChainHandleRequest(request);
    }

    @Override
    public Chain handleFailure(JsonObject request) {
        return new ChainHandleFailure(request);
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
