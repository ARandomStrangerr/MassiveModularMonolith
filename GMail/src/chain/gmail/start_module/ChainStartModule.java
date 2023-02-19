package chain.gmail.start_module;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        chain.add(new LinkSetConstance(this));
        chain.add(new LinkConnectToDataStream(this));
    }
}
