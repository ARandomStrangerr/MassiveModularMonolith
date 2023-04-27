package chain.connection_manager;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessOutGoingRequest extends Chain {
    public ChainProcessOutGoingRequest(JsonObject processObject) {
        super(processObject);
        addLink(new LinkSendToClient(this),
            new LinkTerminateSocketFromClient(this));
    }
}
