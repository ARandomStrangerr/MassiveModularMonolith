import chain.data_stream.start_module.ChainStartModule;
import com.google.gson.JsonObject;

public class DataStream {
    public static void main(String[] args) {
        int port = 9999;
        int timeout = 5000;
        String keyStorePassword = "hungcom23";
        String keyStorePath = "key.jks";
        String moduleName = "DataStream";
        JsonObject information = new JsonObject();
        information.addProperty("port",port);
        information.addProperty("timeout",timeout);
        information.addProperty("keyStorePassword",keyStorePassword);
        information.addProperty("keyStorePath",keyStorePath);
        information.addProperty("moduleName", moduleName);
        new ChainStartModule(information).execute();
    }
}
