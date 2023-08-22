package chain.public_fianace;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.PublicFinance;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.HashMap;

public class LinkLogin extends Link {
	public LinkLogin(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		String username, password;
		try {
			username = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
		} catch (NullPointerException e) {
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Thiếu tên đăng nhập");
			return false;
		}
		try{
			password = chain.getProcessObject().get("body").getAsJsonObject().get("password").getAsString();
		} catch (NullPointerException e){
			chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Thiếu mật khẩu");
			return false;
		}
		JsonObject sendObj = new JsonObject();
		sendObj.addProperty("UserName", username);
		sendObj.addProperty("Password", password);
		sendObj.addProperty("ComputerIP", "192.168.1.0");
		sendObj.addProperty("ComputerName","NDVU");
		sendObj.addProperty("BudgetCode","ZZZZZ11");
		sendObj.addProperty("Description","Mô tả");
		HashMap<String, String> map = new HashMap<>();

		try{
			RESTRequest.untrustPost(Url.LOGIN.path, sendObj.toString(),map);
		}catch (IOException e){
			e.printStackTrace();
		}
		chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", "false");
		chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from"));
		try{
			PublicFinance.socketToDataStream.write(chain.getProcessObject().toString());
		}catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
