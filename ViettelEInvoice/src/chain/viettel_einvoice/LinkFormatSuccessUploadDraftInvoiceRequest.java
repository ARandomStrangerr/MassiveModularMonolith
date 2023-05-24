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
		body.addProperty("success", String.format("Thành công tạo %d hóa đơn nháp", chain.getProcessObject().get("body").getAsJsonObject().get("sendData").getAsJsonArray().size()));
		return true;
	}
}
