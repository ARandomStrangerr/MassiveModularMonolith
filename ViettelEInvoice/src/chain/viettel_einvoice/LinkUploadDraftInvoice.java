package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.LinkedHashMap;

public class LinkUploadDraftInvoice extends Link {
	public LinkUploadDraftInvoice(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		// http request header
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("Content-Type", "application/json");
		map.put("Cookies", "access_token=" + chain.getProcessObject().get("body").getAsJsonObject().get("accessToken").getAsString());
		// gson to convert to Json
		Gson gson = new Gson();
		// loop send data
		for (JsonElement ele : chain.getProcessObject().get("body").getAsJsonObject().get("sendData").getAsJsonArray()) {
//			JsonObject returnObject;
//			try {
//				returnObject = gson.fromJson(RESTRequest.post(Url.UploadDraftInvoice.path, ele.toString(), map), JsonObject.class);
//			} catch (IOException e) {
//				e.printStackTrace();
//				continue;
//			}
			System.out.println(ele);
		}
		return true;
	}
}
