package chain.treasury_public_finance.start_module;

import chain.Chain;
import chain.Link;
import memorable.TreasuryPublicFinance;

public class LinkSetInfo extends Link {
    public LinkSetInfo(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        TreasuryPublicFinance.getInstance().name = chain.getProcessObject().get("moduleName").getAsString();
        return true;
    }
}
