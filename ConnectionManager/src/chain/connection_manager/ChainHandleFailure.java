package chain.connection_manager;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleFailure extends Chain {
    public ChainHandleFailure(JsonObject processObject) {
        super(processObject);
        addLink(new LinkLogFailureRequest(this),
            new LinkFormatFailureRequest(this),
            new LinkSendToClient(this),
            new LinkTerminateSocketFromClient(this));
    }
}
