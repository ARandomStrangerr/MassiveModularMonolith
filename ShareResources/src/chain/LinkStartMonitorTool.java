package chain;

import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LinkStartMonitorTool extends Link{
	public LinkStartMonitorTool(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		String moduleName;
		try {
			moduleName = chain.getProcessObject().get("moduleName").getAsString();
		} catch (NullPointerException e){
			System.err.println("moduleName chưa được điền");
			return false;
		}
		String fileName = String.format("%s %s.txt", moduleName, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		MonitorHandler monitorHandler = new MonitorHandler(fileName);
		new Thread(monitorHandler).start();
		JsonObject monitorObject = new JsonObject();
		monitorObject.addProperty("status", true);
		monitorObject.addProperty("notification", "Khởi động hệ thống ghi chép thông tin");
		MonitorHandler.addQueue(monitorObject);
		return true;
	}
}
