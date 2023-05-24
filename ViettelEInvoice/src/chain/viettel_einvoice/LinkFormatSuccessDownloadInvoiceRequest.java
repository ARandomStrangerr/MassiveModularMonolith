package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatSuccessDownloadInvoiceRequest extends Link {
	public LinkFormatSuccessDownloadInvoiceRequest(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		// setups the msg to send back to client
		JsonObject body = new JsonObject();
		String msg = String.format("Thành công lấy hóa đơn về từ số %d - %d", chain.getProcessObject().get("body").getAsJsonObject().get("start").getAsInt(), chain.getProcessObject().get("body").getAsJsonObject().get("end").getAsInt());
		body.addProperty("success", msg);
		chain.getProcessObject().add("body", body);
		// does not need to change the header to anymore since the pass-by-ref change in GetInvoice:34 already did the job
		// change the status of the request to false, so that the socket will close after this msg
		chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false);
		return true;
	}
}
