package chain.gmail.start_module;

import chain.Chain;
import chain.Link;
import memorable.GMail;

public class LinkSetConstance extends Link {
    public LinkSetConstance(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        GMail.getInstance().moduleName = chain.getProcessObject().get("moduleName").getAsString();
        return true;
    }
}
