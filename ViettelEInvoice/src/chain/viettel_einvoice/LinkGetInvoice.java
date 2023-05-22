package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.HashMap;

public class LinkGetInvoice extends Link {
	public LinkGetInvoice(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		HashMap<String, String> maps = new HashMap<>();
		maps.put("Content-Type", "application/json");
		maps.put("Key", "Cookies");
		maps.put("Value", chain.getProcessObject().get("body").getAsJsonObject().get("cookies").getAsString());
		JsonObject sendObject = new JsonObject();
		sendObject.add("supplierTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username"));
		sendObject.add("templateCode", chain.getProcessObject().get("body").getAsJsonObject().get("templateCode"));
		sendObject.addProperty("fileType", "PDF");
		String invoiceSeries = chain.getProcessObject().get("body").getAsJsonObject().get("invoiceSeries").getAsString();
		for (int i = chain.getProcessObject().get("body").getAsJsonObject().get("start").getAsInt(); i <= chain.getProcessObject().get("body").getAsJsonObject().get("end").getAsInt(); i++) {
			sendObject.addProperty("invoiceNo", String.format("%s%d7d", invoiceSeries, i));
			try{
				JsonObject returnObject = new Gson().fromJson(RESTRequest.post(Url.DownloadInvoice.path, sendObject.toString(), maps), JsonObject.class);
			}catch (IOException e){
				e.printStackTrace();
			}catch (JsonSyntaxException e){
				e.printStackTrace();
			}
		}
		return true;
	}
}
