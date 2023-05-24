package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.HashMap;

public class LinkUploadInvoice extends Link {
    public LinkUploadInvoice(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
		// create params for header
		HashMap<String, String> maps = new HashMap<>();
		maps.put("Content-Type", "application/json");
		maps.put("Accept", "application/json");
		maps.put("Cookie", "access_token=" + chain.getProcessObject().get("body").getAsJsonObject().get("accessToken").getAsString());
		Gson gson = new Gson();
		JsonObject updateObject = new JsonObject();
		updateObject.add("header", chain.getProcessObject().get("header"));
		updateObject.get("header").getAsJsonObject().add("to", updateObject.get("header").getAsJsonObject().remove("from"));

		int i = 0;
		for (JsonElement ele : chain.getProcessObject().get("body").getAsJsonObject().get("sendArray").getAsJsonArray()) {
			i++;
			JsonObject returnObj;
			try {
				returnObj = gson.fromJson(RESTRequest.post(Url.UploadInvoice.path, ele.toString(), maps), JsonObject.class);
			} catch (IOException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Khôgn thành công tạo hóa đơn ở giòng số " + i);
				return false;
			}
		}
		return true;
    }
}
