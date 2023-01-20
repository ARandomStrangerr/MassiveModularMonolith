import chain.connection_manager.start_module.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.net.InetAddress;

public class ConnectionManager {
    public static void main(String[] args) {
        String str = "{\"moduleName\":\"ConnectionManager\",\"hostAddress\":\"localhost\",\"hostPort\":9999,\"listenerPort\":9998,\"keyStorePath\":\"key.jks\",\"keyStorePassword\":\"hungcom23\",\"databaseUrl\":\"jdbc:postgresql://localhost:5432/authentication?password=hungcom23\",\"poolSize\":2,\"timeout\":5000}";
        if (args.length == 0) {
            System.err.println("Missing the argument");
            return;
        }
        JsonObject information = new Gson().fromJson(str, JsonObject.class);
        if (!new ChainStartModule(information).execute()) System.exit(2);
    }
}

class TestCase1 {
    public static void main(String[] args) throws Exception {
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"12345678901234567\"}");
        System.out.println(socketWrapper.read());
    }
}

class SMTPMailTester {
    public static void main(String[] args) throws Exception {
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("118.70.127.55"), 8889, "key.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"24:41:8C:06:2C:82\"}");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("job", "SMTPMail");
        jsonObject.addProperty("host", "smtp.office365.com");
        jsonObject.addProperty("port", 587);
        jsonObject.addProperty("username", "tckt@vnua.edu.vn");
        jsonObject.addProperty("password", "qtcgpjbjjsvdgbyy");
        jsonObject.addProperty("recipient", "billslim0996@gmail.com");
        jsonObject.addProperty("subject", "testing email");
        jsonObject.addProperty("message", "an email to test the service working or not");
        socketWrapper.write(jsonObject.toString());
        System.out.println(socketWrapper.read());
        Thread.sleep(5000);
    }
}

class TestCase3 {
    public static void main(String[] args) throws Exception {
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9998, "key.jks", "hungcom23");
        socketWrapper.write("{macAddress:\"24:41:8C:06:2C:82\"}");
        System.out.println(socketWrapper.read());
        Thread.sleep(5000);
    }
}
