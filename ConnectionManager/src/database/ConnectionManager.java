package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager extends ConnectionPool{
    public ConnectionManager(String url, int poolSize) throws SQLException {
        super(url, poolSize);
    }

    public boolean getLoginInfo(String macAddress) throws SQLException {
        PrepareStatementInterface prepareStatementInterface = connection -> {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM authentication WHERE mac=?");
            statement.setString(1, macAddress.toUpperCase());
            return statement;
        };
        ResultSet result = super.executePreparedStatement(prepareStatementInterface);
        return result.next();
    }
}
