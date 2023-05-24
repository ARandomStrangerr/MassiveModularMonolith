package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainDownloadInvoice extends Chain {
	public ChainDownloadInvoice(JsonObject processObject) {
		super(processObject);
		super.addLink(new LinkGetAuthenticate(this),
			new LinkDownloadInvoice(this),
			new LinkFormatSuccessDownloadInvoiceRequest(this));
	}
}
