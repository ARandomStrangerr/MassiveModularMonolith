import chain.treasury_public_finance.start_module.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.net.InetAddress;

public class TreasuryPublicFinance {
    public static void main(String[] args) {
        JsonObject initialObject = new Gson().fromJson(args[0], JsonObject.class);
        if(!new ChainStartModule(initialObject).execute()) System.exit(-1);
    }
}

class Test1{
    public static void main(String[] args) throws Exception{
        SocketWrapper socket = new SocketWrapper(InetAddress.getByName("localhost"),9998,"key.jks", "hungcom23");
        socket.write("{\"macAddress\":\"24:41:8C:06:2C:82\"}");
        socket.write("{job:\"TreasuryPublicFinance\"}");
    }
}
