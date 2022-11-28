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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mac_address WHERE mac_address=?");
            statement.setString(1, macAddress);
            return statement;
        };
        ResultSet result = super.executePreparedStatement(prepareStatementInterface);
        return result.next();
    }
}
