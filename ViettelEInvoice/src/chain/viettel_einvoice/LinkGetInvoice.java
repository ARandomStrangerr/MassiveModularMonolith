package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import memorable.ViettelEInvoice;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.HashMap;

public class LinkGetInvoice extends Link {
	public LinkGetInvoice(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		// create map
		HashMap<String, String> maps = new HashMap<>();
		maps.put("Content-Type", "application/json");
		maps.put("Accept", "application/json");
		maps.put("Cookie", "access_token=" + chain.getProcessObject().get("body").getAsJsonObject().get("accessToken").getAsString());
		// create send json template
		JsonObject sendObject = new JsonObject();
		sendObject.add("supplierTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username"));
		sendObject.add("templateCode", chain.getProcessObject().get("body").getAsJsonObject().get("templateCode"));
		sendObject.addProperty("fileType", "PDF");
		String invoiceSeries = chain.getProcessObject().get("body").getAsJsonObject().get("invoiceSeries").getAsString();
		// create update json template
		JsonObject updateObject = new JsonObject();
		updateObject.add("header", chain.getProcessObject().get("header")); // this is passed by ref. therefore the next step remove and change also change the processing json
		updateObject.get("header").getAsJsonObject().add("to", updateObject.get("header").getAsJsonObject().remove("from"));
		for (int i = chain.getProcessObject().get("body").getAsJsonObject().get("start").getAsInt(); i <= chain.getProcessObject().get("body").getAsJsonObject().get("end").getAsInt(); i++) {
			sendObject.addProperty("invoiceNo", String.format("%s%07d", invoiceSeries, i));
			JsonObject returnObject;
			try {
				returnObject = new Gson().fromJson(RESTRequest.post(Url.DownloadInvoice.path, sendObject.toString(), maps), JsonObject.class);
			} catch (IOException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không tìm thấy hóa đơn" + sendObject.get("invoiceNo").getAsString());
				return false;
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return false;
			}
			JsonObject bodyUpdateObject = new JsonObject();
			bodyUpdateObject.add("fileName", returnObject.remove("fileName"));
			bodyUpdateObject.add("fileToBytes", returnObject.remove("fileToBytes"));
			updateObject.add("body", bodyUpdateObject);
			try {
				ViettelEInvoice.socketToDataStream.write(updateObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
