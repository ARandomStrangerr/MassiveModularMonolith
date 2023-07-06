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
		try {
			maps.put("Cookie", String.format("access_token=%s", getToken()));
		} catch (IOException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
			return false;
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
			return false;
		}
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
			// check the return message
			if (returnObj.has("result")) { // case when nothing happened
			} else if (returnObj.has("status") && returnObj.get("status").getAsInt() == 500) { // when the oauth is expired
				// re-obtain the token
				try {
					maps.put("Cookie", "access_token=" + getToken());
				} catch (IOException e) {
					chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
					return false;
				}
				// send the data again
				try {
					returnObj = gson.fromJson(RESTRequest.post(address, ele.toString(), maps), JsonObject.class);
				} catch (IOException e) {
					chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thành công kết nối với máy chủ Viettel ở giòng số " + index);
					return false;
				}
			} else if (returnObj.has("code")) { // check if the response is error code
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại giòng số %d trong tệp tin tải lên, %s", index, returnObj.get("data")));
				return false;
			}
			// notify on server about the uploaded invoice
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", true);
			monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải lên hoá đơn với số %s", username, returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString()));
			MonitorHandler.addQueue(monitorObject);
			// send an update information to the client
			bodyUpdateObject.addProperty("update", "Thành công tải lên hoá đơn với mã số " + returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString());
			try {
				ViettelEInvoice.socketToDataStream.write(updateObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chain.getProcessObject().get("body").getAsJsonObject().addProperty("totalNumber", index);
		return true;
	}

	private String getToken() throws IOException {
		HashMap<String, String> property = new HashMap<>();
		property.put("Content-Type", "application/json");
		String sendData = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",
			chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString(),
			chain.getProcessObject().get("body").getAsJsonObject().get("password").getAsString());
		String temp = RESTRequest.post(Url.Authenticate.path, sendData, property);
		return new Gson().fromJson(temp, JsonObject.class).get("access_token").getAsString();
	}
}
