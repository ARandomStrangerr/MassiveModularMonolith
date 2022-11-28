import chain.connection_manager.start_module.ChainStartModule;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.net.InetAddress;

public class ConnectionManager {
    public static void main(String[] args) {
        JsonObject information = new JsonObject();
        information.addProperty("moduleName", "ConnectionManager");
        information.addProperty("hostAddress", "localhost");
        information.addProperty("hostPort", 9999);
        information.addProperty("listenerPort", 9998);
        information.addProperty("keyStorePassword","hungcom23");
        information.addProperty("keyStorePath","/Users/thanhdo/tlskey.jks");
        information.addProperty("databaseUrl", "jdbc:postgresql://localhost:5432/login_info");
        information.addProperty("poolSize", 2);
        information.addProperty("timeout", 5000);
        new ChainStartModule(information).execute();
    }
}

class TestCase1 {
    public static void main(String[] args) throws Exception{
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "/Users/thanhdo/tlskey.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"12345678901234567\"}");
        System.out.println(socketWrapper.read());
    }
}
