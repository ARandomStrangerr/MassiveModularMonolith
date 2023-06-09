package socket_handler.connection_manager;

import chain.Chain;
import chain.connection_manager.ChainProcessOutGoingRequest;
import memorable.ConnectionManager;
import socket_handler.SocketWrapper;
import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

import java.io.IOException;

public class SocketHandler extends socket_handler.SocketHandler {
	private final String moduleName;
    public SocketHandler(SocketWrapper socket, String moduleName) {
        super(socket);
		this.moduleName = moduleName;
    }

    @Override
    public Chain handleRequest(JsonObject request) {
        return new ChainProcessOutGoingRequest(request);
    }

    @Override
    public Chain handleFailure(JsonObject request) {
        return null;
    }

    @Override
    public boolean handleAuthentication() {
        // write the name of this module to the DataStream module
        try {
            socket.write("{moduleName:\"" + moduleName + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        socket.setName("DataStream");
        return true;
    }

    public void cleanup(){
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", false);
        monitorObject.addProperty("notification", "Mất kết nối với DataStream");
        MonitorHandler.addQueue(monitorObject);
        System.exit(1);
    }
}
