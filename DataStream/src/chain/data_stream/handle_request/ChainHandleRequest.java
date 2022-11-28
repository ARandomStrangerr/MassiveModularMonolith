package chain.data_stream.handle_request;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleRequest extends Chain {
    public ChainHandleRequest(JsonObject processObject, String fromModuleName) {
        super(processObject);
        chain.add(new LinkFormatRequest(this, fromModuleName));
        chain.add(new LinkSendToModule(this));
    }

}
