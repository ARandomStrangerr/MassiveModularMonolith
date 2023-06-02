package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainUploadInvoice extends Chain {
    public ChainUploadInvoice(JsonObject processObject) {
        super(processObject);
        super.addLink(new LinkMaterializeExcelFile(this),
                new LinkReadFromExcel(this),
                new LinkGetAuthenticate(this),
                new LinkUploadInvoice(this),
                new LinkLogSuccessUploadInvoiceRequest(this),
                new LinkFormatSuccessUploadInvoiceRequest(this));
    }
}
