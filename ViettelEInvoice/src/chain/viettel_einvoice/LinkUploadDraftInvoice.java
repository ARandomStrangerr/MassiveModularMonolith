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
import java.util.LinkedHashMap;

public class LinkUploadDraftInvoice extends Link {
	public LinkUploadDraftInvoice(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		String username;
		try {
			username = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Thiếu trường thông tin tên đăng nhập");
			return false;
		}
		String password;
		try {
			password = chain.getProcessObject().get("body").getAsJsonObject().get("password").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Thiếu trường thông tin mật khẩu đăng nhập đăng nhập");
			return false;
		}
		// http request header
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("Content-Type", "application/json");
		try {
			map.put("Cookie", String.format("access_token=" + getToken(username, password)));
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
		// loop send data
		Gson gson = new Gson(); // gson to convert to Json
		int index = 0; // index of current sending invoice
		String address = Url.UploadDraftInvoice.path + username; // address which to be used to send the invoice
		JsonArray sendArray = chain.getProcessObject().get("body").getAsJsonObject().remove("sendData").getAsJsonArray(); // the data actually being sent, remove this one so when logging out, it would not create a long message
		for (JsonElement ele : sendArray) {
			index++;
			JsonObject returnObject;
			// send the request
			try {
				returnObject = gson.fromJson(RESTRequest.post(address, ele.toString(), map), JsonObject.class);
			} catch (RuntimeException e) {
				returnObject = gson.fromJson(e.getMessage(), JsonObject.class);
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại dòng số %d trong tệp tin tải lên, %s", index, returnObject.get("data").getAsString()));
				return false;
			} catch (IOException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại dòng số %d trong tệp tin tải lên, %s", index, e.getMessage()));
				return false;
			}
			// check if the return message is an error
			if (returnObject.has("status") && returnObject.get("status").getAsInt() == 500) { // when the oauth is expired
				try {
					map.put("Cookie", "access_token=" + getToken(username, password));
				} catch (IOException e) {
					chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
					return false;
				}
				try {
					returnObject = gson.fromJson(RESTRequest.post(address, ele.toString(), map), JsonObject.class);
				} catch (IOException e) {
					chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại dòng số %d trong tệp tin tải lên, %s", index, returnObject.get("data").getAsString()));
					return false;
				}
			}
			// print out update message on the server side
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", true);
			monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải hoá đơn nháp thứ %d", username, index));
			MonitorHandler.addQueue(monitorObject);
			// send an update message to the client
			bodyUpdateObject.addProperty("update", String.format("Thành công tải lên hoá đơn nháp thứ %d", index));
			try {
				ViettelEInvoice.socketToDataStream.write(updateObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chain.getProcessObject().get("body").getAsJsonObject().addProperty("totalNumber", index);
		return true;
	}

	private String getToken(String username, String password) throws IOException {
		HashMap<String, String> property = new HashMap<>();
		property.put("Content-Type", "application/json");
		String sendData = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
		String temp = RESTRequest.post(Url.Authenticate.path, sendData, property);
		return new Gson().fromJson(temp, JsonObject.class).get("access_token").getAsString();
	}
}
