package chain.gmail;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.GMail;

public class LinkGetPermission extends Link {
    public LinkGetPermission(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        // get socket

        // set authorized params
        JsonObject authorizedParams = new JsonObject();
        authorizedParams.addProperty("client_id", GMail.getInstance().clientId);
//        authorizedParams.addProperty("redirect_uri", String.format());
//        authorizedParams.addProperty();
        return false;
    }
}
