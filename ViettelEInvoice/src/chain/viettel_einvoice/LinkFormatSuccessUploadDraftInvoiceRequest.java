package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatSuccessUploadDraftInvoiceRequest extends Link {
	public LinkFormatSuccessUploadDraftInvoiceRequest(Chain chain) {
		super(chain);
	}

	@Override
	public boolean execute() {
		JsonObject body = new JsonObject();
		body.addProperty("success", String.format("Thành công tạo %d hóa đơn nháp", chain.getProcessObject().get("body").getAsJsonObject().get("totalNumber").getAsInt()));
		chain.getProcessObject().add("body", body); // update the body
		chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false); // change status to false so the connection manager close the socket
		chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from")); // send back the request where it comes from
		return true;
	}
}
