package chain.connection_manager.start_module;

import chain.Chain;
import chain.Link;
import database.ConnectionManager;

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
        System.out.printf("connected to the database at %s\n", url);
        return true;
    }
}
