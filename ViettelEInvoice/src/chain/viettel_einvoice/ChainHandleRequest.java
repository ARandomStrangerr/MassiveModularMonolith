package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleRequest extends Chain {
    public ChainHandleRequest(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkChooseChain(this),
			new LinkReturnRequest(this));
    }
}
