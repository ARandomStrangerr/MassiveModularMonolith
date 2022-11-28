package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainIncomingConnection extends Chain {
    public ChainIncomingConnection(JsonObject processObject) {
        super(processObject);
    }
}
