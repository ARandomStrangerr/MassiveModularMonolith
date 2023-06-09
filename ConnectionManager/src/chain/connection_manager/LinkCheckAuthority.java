package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;

import java.sql.SQLException;

public class LinkCheckAuthority extends Link {
    public LinkCheckAuthority(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try {
            if (!ConnectionManager.connectionPool.getLoginInfo(chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString(), chain.getProcessObject().get("header").getAsJsonObject().get("to").getAsString().toLowerCase()))
                throw new Exception("client has no privilege");
        } catch (SQLException e) {
            chain.getProcessObject().addProperty("error", "Phần mềm gặp vấn đề, vui lòng liên hệ với người lập trình để được giải quyết");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            chain.getProcessObject().addProperty("error", "Người dùng không được ủy quyền sử dụng phần mềm");
            return false;
        }
        return true;
    }
}
