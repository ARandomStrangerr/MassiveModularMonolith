package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessRequest extends Chain {
    public ChainProcessRequest(JsonObject processObject, String socketName) {
        super(processObject);
        super.chain.add(new LinkCheckAuthority(this, socketName));
        super.chain.add(new LinkFormatRequest(this, socketName));
        super.chain.add(new LinkSendRequest(this));
    }
}
