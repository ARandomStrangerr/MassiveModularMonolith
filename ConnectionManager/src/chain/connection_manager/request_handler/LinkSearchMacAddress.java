package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;

import java.sql.SQLException;

public class LinkSearchMacAddress extends Link {
    public LinkSearchMacAddress(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String mac = chain.getProcessObject().get("macAddress").getAsString().toLowerCase();
        try {
            ConnectionManager.getInstance().database.getLoginInfo(mac);
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
