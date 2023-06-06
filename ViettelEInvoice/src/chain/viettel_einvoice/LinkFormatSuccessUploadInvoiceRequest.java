package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatSuccessUploadInvoiceRequest extends Link {
    public LinkFormatSuccessUploadInvoiceRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject body = new JsonObject();
        body.addProperty("success", String.format("Thành công tải lên %d hoá đơn", chain.getProcessObject().get("body").getAsJsonObject().get("sendData").getAsJsonArray().size()));
        chain.getProcessObject().add("body", body); // setup the new body
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false); // set status to false so that the Conenction Manager will terminate the thread later on
        chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from")); // send the request back to where it comes from
        return true;
    }
}
