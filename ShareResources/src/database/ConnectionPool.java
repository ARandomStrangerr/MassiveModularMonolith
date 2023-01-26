package database;

import java.sql.*;
import java.util.LinkedList;

public abstract class ConnectionPool {
    private final LinkedList<Connection> connectionPool;
    private final LinkedList<ConnectionPool> queue;

    public ConnectionPool(String url, int poolSize) throws SQLException {
        connectionPool = new LinkedList<>();
        queue = new LinkedList<>();
        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(DriverManager.getConnection(url));
        }
    }

    protected ResultSet executePreparedStatement(PrepareStatementInterface prepareStatementInterface) throws SQLException, InterruptedException {
        // remove a connection from the pool
        Connection con = getConnection();
        PreparedStatement preparedStatement = prepareStatementInterface.executeStatement(con);
        ResultSet result = preparedStatement.executeQuery();
        // return the connection to the pool
        returnConnection(con);
        return result;
    }

    private synchronized Connection getConnection() throws InterruptedException {
        while (true) {
            try {
                if (connectionPool.size() == 0 || queue.size() != 0) {
                    queue.add(this);
                    wait();
                }
                return connectionPool.removeFirst();
            } catch (NullPointerException e){
                continue;
            }
        }
    }

    private synchronized void returnConnection(Connection con) {
        connectionPool.add(con);
        if (queue.size() != 0) queue.removeFirst().notify();
    }
}
