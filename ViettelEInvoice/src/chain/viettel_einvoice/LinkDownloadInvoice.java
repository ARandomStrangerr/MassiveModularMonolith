package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import memorable.ViettelEInvoice;
import socket_handler.RESTRequest;
import system_monitor.MonitorHandler;

import java.io.IOException;
import java.util.HashMap;

public class LinkDownloadInvoice extends Link {
	public LinkDownloadInvoice(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		// get the username
		String username;
		try {
			username = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin tên đăng nhập không tồn tại");
			return false;
		}
		// get the password
		String password;
		try {
			password = chain.getProcessObject().get("body").getAsJsonObject().get("password").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin mật khẩu không tồn tại");
			return false;
		}
		// create map
		HashMap<String, String> maps = new HashMap<>();
		maps.put("Content-Type", "application/json");
		maps.put("Accept", "application/json");
		try { // get the token
			maps.put("Cookie", "access_token=" + getToken(username, password));
		} catch (RuntimeException e) { // the connection return an error code
			JsonObject returnObject = new Gson().fromJson(e.getMessage(), JsonObject.class);
			if (returnObject.get("status").getAsInt() == 401)
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
			else chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", e.getMessage());
			return false;
		} catch (IOException e) { // cannot connect to the address
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
			return false;
		}
		// create send json template
		JsonObject sendObject = new JsonObject();
		sendObject.addProperty("supplierTaxCode", username);
		try {
			sendObject.add("templateCode", chain.getProcessObject().get("body").getAsJsonObject().get("templateCode"));
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin mẫu hóa đơn không tồn tại");
			return false;
		}
		sendObject.addProperty("fileType", "PDF");
		String invoiceSeries;
		try {
			invoiceSeries = chain.getProcessObject().get("body").getAsJsonObject().get("invoiceSeries").getAsString();
		}catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin kí hiệu hóa đơn không tồn tại");
			return false;
		}
		// create update json template
		JsonObject updateObject = new JsonObject();
		updateObject.add("header", chain.getProcessObject().get("header").deepCopy());
		updateObject.get("header").getAsJsonObject().add("to", updateObject.get("header").getAsJsonObject().remove("from"));
		// Gson to convert data to JsonObject
		Gson gson = new Gson();
		int startNum;
		try {
			startNum = chain.getProcessObject().get("body").getAsJsonObject().get("start").getAsInt();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin số hóa đơn bắt đầu không tồn tại");
			return false;
		}
		int endNum;
		try {
			endNum = chain.getProcessObject().get("body").getAsJsonObject().get("end").getAsInt();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Trường thông tin số hóa đơn kết thúc không tồn tại");
			return false;
		}
		for (int i = startNum; i <= endNum; i++) {
			String invoiceNumber = String.format("%s%d", invoiceSeries, i);
			sendObject.addProperty("invoiceNo", invoiceNumber);
			JsonObject returnObject;
			try {
				returnObject = gson.fromJson(RESTRequest.post(Url.DownloadInvoice.path, sendObject.toString(), maps), JsonObject.class);
			} catch (JsonSyntaxException e) { // when the json can not be parsed
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Định dạng thông tin gửi về từ Viettel không chính xác");
				return false;
			} catch (RuntimeException e) { // when the post request return with an error code
				returnObject = gson.fromJson(e.getMessage(), JsonObject.class);
				chain.getProcessObject().get("body").getAsJsonObject().add("error", returnObject.remove("data"));
				return false;
			} catch (IOException e) { // when cannot connect to the address
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không kết nối được đến máy chủ Viettel");
				return false;
			}
			JsonObject bodyUpdateObject = new JsonObject();
			JsonObject monitorObject = new JsonObject();
			if (returnObject.has("fileName") && returnObject.has("fileToBytes")) { // first case when nothing goes wrong
				bodyUpdateObject.add("fileName", returnObject.remove("fileName"));
				bodyUpdateObject.add("fileToBytes", returnObject.remove("fileToBytes"));
				monitorObject.addProperty("status", true);
				monitorObject.addProperty("notification", String.format("Đơn vi với mã số thuế %s lấy hóa đơn %s", username, invoiceNumber));
			} else if (returnObject.has("status") && returnObject.get("status").getAsInt() == 500) { // third case when the oauth is expired
				try {
					maps.put("Cookie", "access_token=" + getToken(username, password));
				} catch (IOException e) {
					chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
					return false;
				}
				i--;
				continue;
			} else {
				System.out.println(returnObject);
			}
			updateObject.add("body", bodyUpdateObject);
			try {
				ViettelEInvoice.socketToDataStream.write(updateObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			MonitorHandler.addQueue(monitorObject);
		}
		return true;
	}

	private String getToken(String username, String password) throws IOException, RuntimeException {
		HashMap<String, String> property = new HashMap<>();
		property.put("Content-Type", "application/json");
		String sendData = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
		String temp = RESTRequest.post(Url.Authenticate.path, sendData, property);
		return new Gson().fromJson(temp, JsonObject.class).get("access_token").getAsString();
	}
}
