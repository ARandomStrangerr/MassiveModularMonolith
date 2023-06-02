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
		// send the request back to where it comes from
		chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from"));
		// change the status of the request to false, so that the socket will close after this msg
		chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false);
		return true;
	}
}
