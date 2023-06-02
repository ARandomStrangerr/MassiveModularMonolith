package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

public class LinkLogSuccessUploadDraftInvoiceRequest extends Link {
    public LinkLogSuccessUploadDraftInvoiceRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải lên %d hoá đơn nháp", chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString(), chain.getProcessObject().get("body").getAsJsonObject().get("sendData").getAsJsonArray().size()));
        MonitorHandler.addQueue(monitorObj);
        return false;
    }
}
