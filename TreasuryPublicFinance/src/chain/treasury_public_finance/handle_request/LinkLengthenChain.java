package chain.treasury_public_finance.handle_request;

import chain.Chain;
import chain.Link;

public class LinkLengthenChain extends Link {
    public LinkLengthenChain(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        switch(chain.getProcessObject().get("body").getAsJsonObject().get("action").getAsString()){
            case "checkDocumentStatus":
                break;
            case "sendDocument":
                break;
            case "upload":
                break;
            default:
                return false;
        }
        return true;
    }
}
