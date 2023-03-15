package chain.connection_manager.request_handler;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessIncomeRequest extends Chain {
    public ChainProcessIncomeRequest(JsonObject processObject, String socketName) {
        super(processObject);
        addLink(new LinkFormatRequest(this, socketName), // reformat the input json
            new LinkCheckAuthority(this, socketName), // check for the requested job is available for the computer
            new LinkSendRequest(this)); // send the request to the data stream
    }
}
