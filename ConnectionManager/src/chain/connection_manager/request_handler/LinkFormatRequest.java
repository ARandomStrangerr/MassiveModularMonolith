package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.Map;

public class LinkFormatRequest extends Link {
    private final String socketName;

    public LinkFormatRequest(Chain chain, String socketName) {
        super(chain);
        this.socketName = socketName;
    }

    @Override
    public boolean execute() {
        JsonObject header = new JsonObject(),
                body = new JsonObject();
        header.addProperty("clientId", socketName); // write down the client id
        header.addProperty("status", true); // write down the initial status of the request
        try { // check if the field job is in the json
            if (!chain.getProcessObject().has("job")) {
                throw new Exception("the to field is empty");
            }
            header.add("to", chain.getProcessObject().remove("job")); // move the "job" phrase to "to"
            // indexing information
            LinkedList<String> lst = new LinkedList<>();
            for (Map.Entry<String, JsonElement> a : chain.getProcessObject().entrySet()) lst.add(a.getKey());
            // remove keys and put them into the "body"
            for (String key : lst) body.add(key, chain.getProcessObject().remove(key));
        } catch (Exception e) {
            header.addProperty("status", false);
            body.addProperty("error", "Trường dữ liệu công việc sử lý thông tin bị bỏ trống");
            System.err.println("The 'job' field is empty");
            e.printStackTrace();
            return false;
        } finally {
            chain.getProcessObject().add("header", header);
            chain.getProcessObject().add("body", body);
        }
        return true;
    }
}
