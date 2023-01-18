package chain.data_stream.handle_request;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.DataStream;

public class LinkFormatFailRequest extends Link {
    public LinkFormatFailRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject newBody = new JsonObject(),
            header = chain.getProcessObject().get("header").getAsJsonObject();
        newBody.add("error", chain.getProcessObject().remove("error"));
        chain.getProcessObject().add("body", newBody);
        header.add("to", header.remove("from"));
        header.addProperty("from", DataStream.getInstance().getModuleName());
        return true;
    }
}
