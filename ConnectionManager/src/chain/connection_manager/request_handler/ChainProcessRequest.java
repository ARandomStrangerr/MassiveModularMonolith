package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessRequest extends Chain {
    public ChainProcessRequest(JsonObject processObject) {
        super(processObject);
        super.chain.add(new LinkFormatRequest(this));
        super.chain.add(new LinkSendRequest(this));
    }
}
