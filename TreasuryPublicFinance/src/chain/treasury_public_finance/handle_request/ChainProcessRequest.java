package chain.treasury_public_finance.handle_request;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessRequest extends Chain {
    public ChainProcessRequest(JsonObject processObject) {
        super(processObject);
        addLink(new LinkLogin(this));
    }
}
