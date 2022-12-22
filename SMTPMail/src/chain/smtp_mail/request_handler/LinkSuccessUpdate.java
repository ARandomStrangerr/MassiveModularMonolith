package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkSuccessUpdate extends Link {
    public LinkSuccessUpdate(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject body = new JsonObject();
        body.addProperty("success", "Thành công gửi thư đến địa chỉ ");
        chain.getProcessObject().add("body", body);
        return true;
    }
}
