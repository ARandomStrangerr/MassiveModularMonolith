package chain.connection_manager;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;

class LinkSetInfo extends Link {
    LinkSetInfo(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        ConnectionManager.getInstance().moduleName = chain.getProcessObject().get("moduleName").getAsString();
        return true;
    }
}
