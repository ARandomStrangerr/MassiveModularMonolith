import chain.viettel_einvoice.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ViettelEInvoice {
    public static void main(String[] args) {
		JsonObject startObject = new Gson().fromJson(args[0], JsonObject.class);
        if (!new ChainStartModule(startObject).execute()) System.exit(2);
    }
}
