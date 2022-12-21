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
        information.addProperty("keyStorePath","key.jks");
        information.addProperty("keyStorePassword","hungcom23");
        information.addProperty("databaseUrl", "jdbc:postgresql://localhost:5432/authentication?password=hungcom23");
        information.addProperty("poolSize", 2);
        information.addProperty("timeout", 5000);
        if(!new ChainStartModule(information).execute()) System.exit(2);
    }
}

class TestCase1 {
    public static void main(String[] args) throws Exception{
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"12345678901234567\"}");
        System.out.println(socketWrapper.read());
    }
}

class TestCase2 {
    public static void main(String[] args) throws Exception{
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"24:41:8C:06:2C:82\"}");
        socketWrapper.write("{job:\"smtpmail\"}");
        System.out.println(socketWrapper.read());
        Thread.sleep(5000);
    }
}

class TestCase3 {
    public static void main(String[] args) throws Exception{
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"24:41:8C:06:2C:82\"}");
        System.out.println(socketWrapper.read());
        Thread.sleep(5000);
    }
}