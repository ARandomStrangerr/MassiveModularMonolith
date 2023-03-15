package chain.smtp_mail.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessRequest extends Chain {
    public ChainProcessRequest(JsonObject processObject) {
        super(processObject);
        addLink(new LinkSendQueue(this));
    }
}
