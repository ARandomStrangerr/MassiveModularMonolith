package chain.connection_manager;

import chain.Chain;
import chain.LinkStartMonitorTool;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        addLink(new LinkStartMonitorTool(this),
            new LinkSetInfo(this),
            new LinkStartConnectionPool(this),
            new LinkConnectToDataStream(this),
            new LinkStartListener(this));
    }
}
