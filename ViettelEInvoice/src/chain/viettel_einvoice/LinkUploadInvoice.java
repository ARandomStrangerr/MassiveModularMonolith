package chain.viettel_einvoice;

import aux.viettel_einvoice.Url;
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
		// get username
		String username;
		try {
			username = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không tìm thấy trường dữ liệu tên đăng nhập");
			return false;
		}
		// get password
		String password;
		try {
			password = chain.getProcessObject().get("body").getAsJsonObject().get("password").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không tìm thấy trường dữ liệu mật khẩu đăng nhập");
			return false;
		}
		// create params for header
		HashMap<String, String> maps = new HashMap<>();
		maps.put("Content-Type", "application/json");
		maps.put("Accept", "application/json");
		try {
			maps.put("Cookie", String.format("access_token=%s", getToken(username, password)));
		} catch (IOException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
			return false;
		} catch (RuntimeException e) {
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
		String address = Url.UploadInvoice.path + username; // address to send the post request
		int index = 0; // index of the current sending invoice
		JsonArray sendArray = chain.getProcessObject().get("body").getAsJsonObject().remove("sendData").getAsJsonArray();  // the data actually being sent, remove this one so when logging out, it would not create a long message
		for (JsonElement ele : sendArray) {
			index++;
			JsonObject returnObj;
			JsonObject monitorObject = new JsonObject();
			// upload the invoice
			try {
				returnObj = gson.fromJson(RESTRequest.post(address, ele.toString(), maps), JsonObject.class);
				monitorObject.addProperty("status", true);
				monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải lên hoá đơn với số %s", username, returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString()));
				bodyUpdateObject.addProperty("update", "Thành công tải lên hoá đơn với mã số " + returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString());
			} catch (RuntimeException e) { // case when the REST request return an error code
				returnObj = gson.fromJson(e.getMessage(), JsonObject.class);
				String errorMsg;
				if (returnObj.has("data"))
					errorMsg = returnObj.remove("data").getAsString().trim();
				else
					errorMsg = returnObj.remove("message").getAsString().trim();
				switch (errorMsg) {
					case "GENERAL" -> { // when the token is expired
						System.out.println("Token expired");
						// re-obtain the token
						try { // no longer need the RunTimeException since the username and password is already correct
							maps.put("Cookie", String.format("access_token=%s", getToken(username, password)));
						} catch (IOException e1) {
							chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
							return false;
						}
						// send the data again
						try {
							returnObj = gson.fromJson(RESTRequest.post(address, ele.toString(), maps), JsonObject.class);
						} catch (RuntimeException e1) {
							returnObj = gson.fromJson(e.getMessage(), JsonObject.class);
							chain.getProcessObject().get("body").getAsJsonObject().add("error", returnObj.remove("data"));
							return false;
						} catch (IOException e1) {
							chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thành công kết nối với máy chủ Viettel ở giòng số " + index);
							return false;
						}
						monitorObject.addProperty("status", true);
						monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải lên hoá đơn với số %s", username, returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString()));
						bodyUpdateObject.addProperty("update", "Thành công tải lên hoá đơn với mã số " + returnObj.get("result").getAsJsonObject().get("invoiceNo").getAsString());
					}
					case "Request processing failed; nested exception is java.lang.NullPointerException" -> { // cannot find original invoice, the invoice will be skipped
						monitorObject.addProperty("status", false);
						monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s tại dòng số %d không tìm thấy hóa đơn gốc", username, index));
						bodyUpdateObject.addProperty("error", String.format("Tại dòng số %d không tìm thấy hóa đơn gốc", index));
					}
					case default -> { // other errors. this class will be return in this case
						monitorObject.addProperty("status", false);
						monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế tại dòng số %d lỗi: %s", index, errorMsg));
						MonitorHandler.addQueue(monitorObject); // hand out the print message before return
						chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại dòng số %d gặp lỗi: %s", index, errorMsg));
						return false;
					}
				}
			} catch (IOException e) {
				chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thành công kết nối với máy chủ Viettel tại dòng số " + index);
				return false;
			}
			// notify on server about the uploaded invoice
			MonitorHandler.addQueue(monitorObject);
			// send an update information to the client
			try {
				ViettelEInvoice.socketToDataStream.write(updateObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chain.getProcessObject().get("body").getAsJsonObject().addProperty("totalNumber", index);
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
