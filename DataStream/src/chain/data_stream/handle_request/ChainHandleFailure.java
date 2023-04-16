package chain.data_stream.handle_request;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleFailure extends Chain {
    public ChainHandleFailure(JsonObject processObject) {
        super(processObject);
        addLink(new LinkLogFalseRequest(this),
            new LinkFormatFailRequest(this),
            new LinkSendToModule(this));
    }
}
