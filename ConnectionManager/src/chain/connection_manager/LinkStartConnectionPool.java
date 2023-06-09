package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import database.ConnectionManager;
import system_monitor.MonitorHandler;

import java.sql.SQLException;

class LinkStartConnectionPool extends Link {
    LinkStartConnectionPool(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String url = chain.getProcessObject().get("databaseUrl").getAsString();
        int poolSize = chain.getProcessObject().get("poolSize").getAsInt();
        ConnectionManager connectionPool;
        try {
            connectionPool = new ConnectionManager(url, poolSize);
        } catch (SQLException e) {
            JsonObject monitorObject = new JsonObject();
            monitorObject.addProperty("status", false);
            monitorObject.addProperty("notification", String.format("Lỗi %s khi kết nối đến cơ sở dữ liệu", e.getMessage()));
            MonitorHandler.addQueue(monitorObject);
            return false;
        }
        memorable.ConnectionManager.connectionPool = connectionPool;
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Thành cồng kết nối đến cơ sở dữ liệu");
        MonitorHandler.addQueue(monitorObj);
        return true;
    }
}
