package chain.data_stream;

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
        try {
            JsonObject newBody = new JsonObject(),
                header = chain.getProcessObject().get("header").getAsJsonObject();
            // remove the entire body then change it with the error message
            newBody.add("error", chain.getProcessObject().remove("error"));
            chain.getProcessObject().add("body", newBody);
            // swap from and to field;
            header.add("to", header.remove("from"));
            header.addProperty("from", DataStream.getInstance().getModuleName());
        } catch (Exception e){
            e.printStackTrace();
            chain.endEarly();
        }
        return true;
    }
}
