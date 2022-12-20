package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleFailure extends Chain {
    public ChainHandleFailure(JsonObject processObject) {
        super(processObject);
        super.chain.add(new LinkSendToClient(this));
        super.chain.add(new LinkTerminateSocketFromClient(this));
    }
}
