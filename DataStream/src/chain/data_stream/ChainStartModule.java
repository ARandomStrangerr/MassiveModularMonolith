package chain.data_stream;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        addLink(new LinkStartSystemMonitor(this),
            new LinkSetInfo(this),
            new LinkStartListener(this));
    }
}
