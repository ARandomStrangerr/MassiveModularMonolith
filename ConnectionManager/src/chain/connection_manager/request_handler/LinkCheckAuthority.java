package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;

import java.sql.SQLException;

public class LinkCheckAuthority extends Link {
    private final String socketName;
    public LinkCheckAuthority(Chain chain, String socketName) {
        super(chain);
        this.socketName = socketName;
    }

    @Override
    public boolean execute() {
        String job = chain.getProcessObject().get("header").getAsJsonObject().get("to").getAsString();
        try {
            if (!ConnectionManager.getInstance().database.getLoginInfo(socketName, job))
                throw new Exception("client has no privilege");
        } catch (SQLException e) {
            chain.getProcessObject().addProperty("error", "Phần mềm gặp vấn đề, vui lòng liên hệ với người lập trình để được giải quyết");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            JsonObject body = new JsonObject();
            body.addProperty("error", "Người dùng không được ủy quyền sử dụng phần mềm");
            chain.getProcessObject().add("body", body);
            e.printStackTrace();
            return false;
        }
        return true;
    }
}