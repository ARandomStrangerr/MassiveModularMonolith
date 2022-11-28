package chain.connection_manager.start_module;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainStartModule extends Chain {
    public ChainStartModule(JsonObject processObject) {
        super(processObject);
        super.chain.add(new LinkSetInfo(this));
        super.chain.add(new LinkStartConnectionPool(this));
        super.chain.add(new LinkStartSocket(this));
        super.chain.add(new LinkStartListener(this));
    }
}
