import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.io.IOException;
import java.net.InetAddress;

/**
 * test case where the to module does not exits
 */
class TestCase1 {
    public static void main(String[] args) throws Exception {
        SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9999, "/Users/thanhdo/tlskey.jks", "hungcom23");
        socketWrapper.write("{moduleName:\"testModule\"}");
        socketWrapper.write("{header:{to:\"this_module_does't_exist\",status:true}}");
        Thread.sleep(10000);
    }
}

class TestCase2 {
    public static void main(String[] args){
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9999, "/Users/thanhdo/tlskey.jks", "hungcom23");
                socketWrapper.write("{moduleName:\"TestModulePair1\"}");
                JsonObject header = new JsonObject(),
                        body = new JsonObject(),
                        request = new JsonObject();
                header.addProperty("to", "TestModulePair2");
                header.addProperty("status", true);
                body.addProperty("msg", "hello from TestModulePair1");
                request.add("header", header );
                request.add("body",body);
                socketWrapper.write(request.toString());
                System.out.println("ModulePair1 receive: " + socketWrapper.read());
                socketWrapper.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                SocketWrapper socketWrapper = new SocketWrapper(InetAddress.getByName("localhost"), 9999, "/Users/thanhdo/tlskey.jks", "hungcom23");
                socketWrapper.write("{moduleName:\"TestModulePair2\"}");
                String msg = socketWrapper.read();
                System.out.println("ModulePair2 receive: " + msg);
                JsonObject request = new Gson().fromJson(msg, JsonObject.class);
                request.get("body").getAsJsonObject().addProperty("msg", "hello from TestModulePair2");
                request.get("header").getAsJsonObject().addProperty("to","TestModulePair1");
                socketWrapper.write(request.toString());
                socketWrapper.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
