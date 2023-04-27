package chain.connection_manager;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainProcessIncomingRequest extends Chain {
    public ChainProcessIncomingRequest(JsonObject processObject, String socketName) {
        super(processObject);
        addLink(new LinkFormatRequest(this, socketName), // reformat the input json
            new LinkCheckAuthority(this), // check for the requested job is available for the computer
            new LinkSendRequestToDataSteam(this)); // send the request to the data stream
    }
}
