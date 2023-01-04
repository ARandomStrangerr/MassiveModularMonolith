package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.SMTPMail;

import java.io.IOException;

public class LinkUpdate extends Link {
    public LinkUpdate(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject body = new JsonObject();
        body.addProperty("update", String.format("Thành công gởi e-mail đến địa chỉ %s", chain.getProcessObject().get("body").getAsJsonObject().get("recipient").getAsString()));
        chain.getProcessObject().add("body", body);
        chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from"));
        try {
            SMTPMail.getInstance().socket.write(chain.getProcessObject().toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
