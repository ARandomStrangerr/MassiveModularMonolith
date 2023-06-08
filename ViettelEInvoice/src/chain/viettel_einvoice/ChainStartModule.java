package chain.viettel_einvoice;

import chain.Chain;
import chain.LinkStartMonitorTool;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkStartMonitorTool(this),
            new LinkStartSocketToDataStream(this));
    }
}
