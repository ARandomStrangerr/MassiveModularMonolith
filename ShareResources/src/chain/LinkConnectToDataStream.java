package chain;

import com.google.gson.JsonObject;
import socket_handler.SocketWrapper;
import system_monitor.MonitorHandler;

import java.net.InetAddress;

public abstract class LinkConnectToDataStream extends Link {
	public LinkConnectToDataStream(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		String dataStreamAddress;
		try {
			dataStreamAddress = chain.getProcessObject().get("hostAddress").getAsString();
		} catch (NullPointerException e) {
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", false);
			monitorObject.addProperty("notification", "Thiếu địa chỉ của DataStream");
			MonitorHandler.addQueue(monitorObject);
			return false;
		}
		int port;
		try {
			port = chain.getProcessObject().get("hostPort").getAsInt();
		} catch (NullPointerException e) {
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", false);
			monitorObject.addProperty("notification", "Thiếu cổng của DataStream");
			MonitorHandler.addQueue(monitorObject);
			return false;
		}
		String jksPath;
		try {
			jksPath = chain.getProcessObject().get("jksPath").getAsString();
		} catch (NullPointerException e) {
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", false);
			monitorObject.addProperty("notification", "Thiếu đường dẫn đễn tệp tin JKS để kết nối đến DataStream");
			MonitorHandler.addQueue(monitorObject);
			return false;
		}
		String jksPassword;
		try {
			jksPassword = chain.getProcessObject().get("jksPassword").getAsString();
		} catch (NullPointerException e) {
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", false);
			monitorObject.addProperty("notification", "Thiếu đường dẫn đễn tệp tin JKS để kết nối đến DataStream");
			MonitorHandler.addQueue(monitorObject);
			return false;
		}
		SocketWrapper socket;
		try {
			socket = new SocketWrapper(InetAddress.getByName(dataStreamAddress), port, jksPath, jksPassword);
		} catch (Exception e){
			JsonObject monitorObject = new JsonObject();
			monitorObject.addProperty("status", false);
			monitorObject.addProperty("notification", "Không kết nối được đến DataStream " + e.getMessage());
			MonitorHandler.addQueue(monitorObject);
			return false;
		}
		saveSocket(socket);
		new Thread(getSocketHandler(socket)).start();
		JsonObject monitorObject = new JsonObject();
		monitorObject.addProperty("status", true);
		monitorObject.addProperty("notification", String.format("Kết nối đến DataStream tại %s:%d", dataStreamAddress, port));
		MonitorHandler.addQueue(monitorObject);
		return true;
	}

	protected abstract void saveSocket(SocketWrapper socket);

	protected abstract Runnable getSocketHandler(SocketWrapper socket);
}
