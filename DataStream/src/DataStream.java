import chain.data_stream.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DataStream {
    public static void main(String[] args) {
        Gson gson = new Gson();
        if (args.length == 0) {
            System.err.println("Missing the argument");
            return;
        }
        JsonObject information = gson.fromJson(args[0], JsonObject.class);
        if (!new ChainStartModule(information).execute()) System.exit(2);
    }
}
