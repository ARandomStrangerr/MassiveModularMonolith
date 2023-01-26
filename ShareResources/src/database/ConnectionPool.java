package database;

import java.sql.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;

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
        /*
        todo resolve this problem.
        it is quite strange when one thread return connection notify another thread,
        it has 0 available connection in the pool when there is supposed to be 1.
        it is supposed to be:
        0 connection -> a thread return connection ( 1 connection ) -> notify the next thread ( 1 connection )
        -> the next thread claim the connection ( 0 connection )
        instead :
        0 connection -> a thread return connection ( 1 connection ) -> ? something happened here ( 0 connection )
        -> notify the next thread ( 0 connection ) -> the next thread claim the connection ( 0 connection ) ERROR !
         */
        while (true) {
        if (connectionPool.size() == 0 || queue.size() != 0) {
            queue.add(this);
            wait();
        }
            try {
                return connectionPool.removeFirst();
            } catch (NoSuchElementException e){
                continue;
            }
        }
    }

    private synchronized void returnConnection(Connection con) {
        connectionPool.add(con);
        if (queue.size() != 0) queue.removeFirst().notify();
    }
}
