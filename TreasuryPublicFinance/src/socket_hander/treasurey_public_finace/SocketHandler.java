package socket_hander.treasurey_public_finace;

import chain.Chain;
import chain.treasury_public_finance.handle_request.ChainProcessRequest;
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
        return new ChainProcessRequest(request);
    }

    @Override
    public Chain handleFailure(JsonObject request) {
        return null;
    }

    @Override
    public boolean handleAuthentication() {
        try {
            socket.write("{moduleName:\"" + moduleName + "\"}");
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        socket.setName("DataStream");
        return true;
    }

    @Override
    public void cleanup() {
        System.err.printf("Socket to %s has been closed\n",socket.getName());
        System.exit(1);
    }
}
