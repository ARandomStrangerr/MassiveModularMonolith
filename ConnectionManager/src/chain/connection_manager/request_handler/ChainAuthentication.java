package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainAuthentication extends Chain {
    public ChainAuthentication(JsonObject processObject) {
        super(processObject);
        super.chain.add(new LinkSearchMacAddress(this));
    }
}
