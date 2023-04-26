package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ViettelEInvoice;
import system_monitor.MonitorHandler;

import java.io.IOException;

public class LinkReturnRequest extends Link {
    public LinkReturnRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try {
            ViettelEInvoice.socketToDataStream.write(chain.getProcessObject().toString());
        } catch (IOException e){
            JsonObject monitorObj = new JsonObject();
            monitorObj.addProperty("status", false);
            monitorObj.addProperty("notification", "Không thể truyền dữ liệu đến Data Stream");
            monitorObj.add("request", chain.getProcessObject());
            MonitorHandler.addQueue(monitorObj);
        }
        return true;
    }
}
