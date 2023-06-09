package chain;

import com.google.gson.JsonObject;
import socket_handler.ListenerHandler;
import socket_handler.ListenerWrapper;
import system_monitor.MonitorHandler;

public abstract class LinkOpenListener extends Link {
	public LinkOpenListener(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		int port;
		try{
			port = chain.getProcessObject().get("listenerPort").getAsInt();
		} catch (NullPointerException e) {
			System.err.println("Thiểu cổng tiếp nhận kết nối");
			return false;
		}
		String jksPath;
		try {
			jksPath = chain.getProcessObject().get("listenerJksPath").getAsString();
		} catch (NullPointerException e) {
			System.err.println("Thiếu đường dẫn đễn tệp tin JKS để mở cổng tiếp nhận kết");
			return false;
		}
		String jksPassword;
		try {
			jksPassword = chain.getProcessObject().get("listenerJksPassword").getAsString();
		} catch (NullPointerException e) {
			System.err.println("Thiếu đường dẫn đễn tệp tin JKS để mở cổng tiếp nhận kết");
			return false;
		}
		ListenerWrapper listener;
		try {
			listener = new ListenerWrapper(port, jksPath, jksPassword);
		} catch (Exception e) {
			System.err.println("Không mở được cổng tiếp nhận kết vì " + e.getMessage());
			return false;
		}
		saveListener(listener);
		new Thread(getListenerHandler(listener)).start();
		JsonObject monitorObject = new JsonObject();
		monitorObject.addProperty("status", true);
		monitorObject.addProperty("notification", String.format("Mở cổng tiếp nhận kết nối tại %d", port));
		MonitorHandler.addQueue(monitorObject);
		return true;
	}

	abstract protected void saveListener(ListenerWrapper listener);

	abstract protected ListenerHandler getListenerHandler(ListenerWrapper listener);
}
