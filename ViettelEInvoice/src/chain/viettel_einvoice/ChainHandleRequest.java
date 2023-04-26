package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainHandleRequest extends Chain {
    public ChainHandleRequest(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkMaterializeExcelFile(this),
            new LinkReadExcelFile(this),
            new LinkSendInvoice(this));
    }
}
