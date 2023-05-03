package chain.data_stream;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleRequest extends Chain {
    public ChainHandleRequest(JsonObject processObject, String fromModuleName) {
        super(processObject);
        addLink(new LinkFormatRequest(this, fromModuleName),
            new LinkSendToModule(this));
    }

}
