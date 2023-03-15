package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessReturnRequest extends Chain {
    public ChainProcessReturnRequest(JsonObject processObject) {
        super(processObject);
        addLink(new LinkSendToClient(this),
            new LinkTerminateSocketFromClient(this));
    }
}
