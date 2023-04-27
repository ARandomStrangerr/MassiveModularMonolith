package chain.connection_manager;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        addLink(new LinkStartMonitorThread(this),
            new LinkSetInfo(this),
            new LinkStartConnectionPool(this),
            new LinkStartSocket(this),
            new LinkStartListener(this));
    }
}
