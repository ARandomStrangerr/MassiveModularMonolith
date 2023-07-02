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
		// create map
		HashMap<String, String> maps = new HashMap<>();
		maps.put("Content-Type", "application/json");
		maps.put("Accept", "application/json");
		try {
			maps.put("Cookie", "access_token=" + getToken());
		} catch (IOException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
			return false;
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
			return false;
		}
		// create send json template
		JsonObject sendObject = new JsonObject();
		sendObject.add("supplierTaxCode", chain.getProcessObject().get("body").getAsJsonObject().get("username"));
		sendObject.add("templateCode", chain.getProcessObject().get("body").getAsJsonObject().get("templateCode"));
		sendObject.addProperty("fileType", "PDF");
		String invoiceSeries = chain.getProcessObject().get("body").getAsJsonObject().get("invoiceSeries").getAsString();
		// create update json template
		JsonObject updateObject = new JsonObject();
		updateObject.add("header", chain.getProcessObject().get("header").deepCopy());
		updateObject.get("header").getAsJsonObject().add("to", updateObject.get("header").getAsJsonObject().remove("from"));
		// Gson to convert data to JsonObject
		Gson gson = new Gson();
		String clientTaxCode = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
		for (int i = chain.getProcessObject().get("body").getAsJsonObject().get("start").getAsInt(); i <= chain.getProcessObject().get("body").getAsJsonObject().get("end").getAsInt(); i++) {
			String invoiceNumber = String.format("%s%d", invoiceSeries, i);
			sendObject.addProperty("invoiceNo", invoiceNumber);
			JsonObject returnObject;
			try {
				returnObject = gson.fromJson(RESTRequest.post(Url.DownloadInvoice.path, sendObject.toString(), maps), JsonObject.class);
			} catch (IOException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không kết nối được đến máy chủ Viettel");
				return false;
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Định dạng thông tin gửi về từ Viettel không chính xác");
				return false;
			}
			JsonObject bodyUpdateObject = new JsonObject();
			JsonObject monitorObject = new JsonObject();
			if (returnObject.has("fileName") && returnObject.has("fileToBytes")) { // first case when nothing goes wrong
				bodyUpdateObject.add("fileName", returnObject.remove("fileName"));
				bodyUpdateObject.add("fileToBytes", returnObject.remove("fileToBytes"));
				monitorObject.addProperty("status", true);
				monitorObject.addProperty("notification", String.format("Đơn vi với mã số thuế %s lấy hóa đơn %s", clientTaxCode, invoiceNumber));
			} else if (returnObject.has("code")) { // second case when the invoice could not be found
				bodyUpdateObject.addProperty("error", "Không tìm thấy hóa đơn " + invoiceNumber);
				monitorObject.addProperty("status", false);
				monitorObject.addProperty("notification", String.format("Đơn vi với mã số thuế %s không tìm thấy hóa đơn %s", clientTaxCode, invoiceNumber));
			} else if (returnObject.has("status") && returnObject.get("status").getAsInt() == 500) { // third case when the oauth is expired
				try {
					maps.put("Cookie", "access_token=" + getToken());
				} catch (IOException e) {
					chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
					return false;
				}
				i--;
				continue;
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
