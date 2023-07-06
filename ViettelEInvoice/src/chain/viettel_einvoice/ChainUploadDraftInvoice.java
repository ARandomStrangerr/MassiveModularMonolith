package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainUploadDraftInvoice extends Chain {
	public ChainUploadDraftInvoice(JsonObject processObject) {
		super(processObject);
		super.addLink(new LinkMaterializeExcelFile(this),
			new LinkReadFromExcel(this),
			new LinkUploadDraftInvoice(this),
			new LinkLogSuccessUploadDraftInvoiceRequest(this),
			new LinkFormatSuccessUploadDraftInvoiceRequest(this));
	}
}
