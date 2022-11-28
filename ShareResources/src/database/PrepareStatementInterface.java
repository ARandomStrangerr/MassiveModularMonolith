package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementInterface {
    /**
     * pass on method, use as lambda expression.
     * this class will be called in {@link database.ConnectionPool}
     * @param connection
     * @return
     * @throws SQLException
     */
    PreparedStatement executeStatement(Connection connection) throws SQLException;
}
