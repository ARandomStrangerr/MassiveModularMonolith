package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.ViettelEInvoice;
import socket_handler.RESTRequest;
import system_monitor.MonitorHandler;

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
		maps.put("Cookie", String.format("access_token=%s", chain.getProcessObject().get("body").getAsJsonObject().remove("accessToken").getAsString()));
		// update object
		JsonObject updateObject = new JsonObject();
		JsonObject bodyUpdateObject = new JsonObject();
		updateObject.add("header", chain.getProcessObject().get("header").deepCopy());
		updateObject.get("header").getAsJsonObject().add("to", updateObject.get("header").getAsJsonObject().remove("from"));
		updateObject.add("body", bodyUpdateObject);
		// loop send the invoice
		Gson gson = new Gson(); // gson to convert
		String username = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString(); // tax code
		String address = Url.UploadInvoice.path + username; // address to send the post request
		int index = 0; // index of the current sending invoice
		JsonArray sendArray = chain.getProcessObject().get("body").getAsJsonObject().remove("sendData").getAsJsonArray();  // the data actually being sent, remove this one so when logging out, it would not create a long message
		for (JsonElement ele : sendArray) {
			index++;
			JsonObject returnObj;
			// upload the invoice
			try {
				returnObj = gson.fromJson(RESTRequest.post(address, ele.toString(), maps), JsonObject.class);
			} catch (IOException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thành công kết nối với máy chủ Viettel ở giòng số " + index);
				return false;
			}
			// check the return message is an error
			if (returnObj.has("code")) { // check if the response is error code
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại giòng số %d trong tệp tin tải lên, %s", index, returnObj.get("data")));
				return false;
			}
			// send an update information to the client
			try {
				bodyUpdateObject.addProperty("update", "Thành công tải lên hoá đơn với mã số " + returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString());
			} catch (NullPointerException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Viettel gởi về thông tin " + returnObj);
				return false;
			}
			try {
				ViettelEInvoice.socketToDataStream.write(updateObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			// print out an update
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", true);
			monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải lên hoá đơn với số %s", username, returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString()));
			MonitorHandler.addQueue(monitorObject);
		}
		chain.getProcessObject().get("body").getAsJsonObject().addProperty("totalNumber", index);
		return true;
	}
}
