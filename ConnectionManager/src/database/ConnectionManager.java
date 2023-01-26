package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager extends ConnectionPool{
    public ConnectionManager(String url, int poolSize) throws SQLException {
        super(url, poolSize);
    }

    public boolean getLoginInfo(String macAddress, String job) throws SQLException, InterruptedException {
        PrepareStatementInterface prepareStatementInterface = connection -> {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM authentication WHERE (mac=? AND job=?)");
            statement.setString(1, macAddress.toUpperCase());
            statement.setString(2,job.toLowerCase());
            return statement;
        };
        ResultSet result = super.executePreparedStatement(prepareStatementInterface);
        return result.next();
    }
}
