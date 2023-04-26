import chain.viettel_einvoice.ChainStartModule;
import com.google.gson.JsonObject;

public class ViettelEInvoice {
    public static void main(String[] args) {
        JsonObject startObj = new JsonObject();
        startObj.addProperty("moduleName", "ViettelEInvoice");
        startObj.addProperty("hostAddress", "localhost");
        startObj.addProperty("hostPort", 9999);
        startObj.addProperty("jksPath", "key.jks");
        startObj.addProperty("jksPassword","hungcom23");
        if (!new ChainStartModule(startObj).execute()) System.exit(2);
    }
}
