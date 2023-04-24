import chain.viettel_einvoice.ChainStartModule;
import com.google.gson.JsonObject;

public class ViettelEInvoice {
    public static void main(String[] args) {
        JsonObject startObj = new JsonObject();
        startObj.addProperty("moduleName", "ViettelEInvoice");
        new ChainStartModule(startObj);
    }
}
