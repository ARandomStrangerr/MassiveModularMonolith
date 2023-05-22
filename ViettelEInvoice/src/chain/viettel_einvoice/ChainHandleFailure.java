package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleFailure extends Chain {
    public ChainHandleFailure(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkLogFalseRequest(this),
            new LinkFormatFalseRequest(this),
            new LinkSendRequest(this));
    }
}
