import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.net.InetAddress;

class TestDatabaseAuthentication {
    public static void main(String[] args) throws Exception {
        SocketWrapper socket = new SocketWrapper(InetAddress.getByName("localhost"), 9999, "key.jks", "hungcom23");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientId", "abcdef");
        socket.write(jsonObject.toString());
        jsonObject = new JsonObject();

    }
}
