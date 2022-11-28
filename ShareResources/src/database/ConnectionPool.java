package database;

import java.sql.*;
import java.util.LinkedList;

public abstract class ConnectionPool {
    private final LinkedList<Connection> connectionPool;

    public ConnectionPool(String url, int poolSize) throws SQLException {
        connectionPool = new LinkedList<>();
        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(DriverManager.getConnection(url));
        }
    }

    protected ResultSet executePreparedStatement(PrepareStatementInterface prepareStatementInterface) throws SQLException {
        // remove a connection from the pool
        Connection con;
        synchronized (connectionPool) {
            con = connectionPool.removeFirst();
        }
        PreparedStatement preparedStatement = prepareStatementInterface.executeStatement(con);
        ResultSet result = preparedStatement.executeQuery();
        // return the connection to the pool
        synchronized (connectionPool){
            connectionPool.add(con);
        }
        return result;
    }
}
