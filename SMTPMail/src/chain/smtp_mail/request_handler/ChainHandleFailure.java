package chain.smtp_mail.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleFailure extends Chain {
    public ChainHandleFailure(JsonObject processObject) {
        super(processObject);
        addLink(new LinkFormatMessage(this),
            new LinkSendToDataStream(this));
    }
}
