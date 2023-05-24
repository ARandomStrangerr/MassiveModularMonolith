package chain.viettel_einvoice;

import chain.Chain;
import com.google.gson.JsonObject;

public class ChainGetInvoice extends Chain {
	public ChainGetInvoice(JsonObject processObject) {
		super(processObject);
		super.addLink(new LinkGetAuthenticate(this),
			new LinkGetInvoice(this),
			new LinkFormatSuccessGetInvoiceRequest(this));
	}
}
