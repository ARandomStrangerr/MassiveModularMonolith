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
            System.err.println("Cannot connect to the database");
            e.printStackTrace();
            return false;
        }
        memorable.ConnectionManager.getInstance().database = connectionPool;
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Thành cồng kết nối đến cơ sở dữ liệu");
        MonitorHandler.addQueue(monitorObj);
        return true;
    }
}
