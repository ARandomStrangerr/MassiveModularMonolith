package chain.smtp_mail.start_module;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkSetInfo(this),
            new LinkStartSocket(this));
    }
}
