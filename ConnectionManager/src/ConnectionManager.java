import chain.connection_manager.start_module.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;

import java.net.InetAddress;

public class ConnectionManager {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Missing the argument");
            return;
        }
        args[0] ="{\"moduleName\":\"ConnectionManager\",\"hostAddress\":\"localhost\",\"hostPort\":9999,\"listenerPort\":9998,\"keyStorePath\":\"key.jks\",\"keyStorePassword\":\"hungcom23\",\"databaseUrl\":\"jdbc:postgresql://localhost:5432/authentication?password=hungcom23\",\"poolSize\":2,\"timeout\":5000}";
        JsonObject information = new Gson().fromJson(args[0], JsonObject.class);
        if (!new ChainStartModule(information).execute()) System.exit(2);
    }
}
