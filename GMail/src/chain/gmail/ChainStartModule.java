package chain.gmail;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkStartSystemMonitor(this),
            new LinkStartSocket(this));
    }
}
