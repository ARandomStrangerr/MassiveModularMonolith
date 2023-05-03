package chain.data_stream;

import chain.Chain;
import chain.Link;

class LinkFormatRequest extends Link {
    private final String fromModuleName;

    LinkFormatRequest(Chain chain, String fromModuleName) {
        super(chain);
        this.fromModuleName = fromModuleName;
    }

    @Override
    public boolean execute() {
        // set header.from field
        try{
            chain.getProcessObject().get("header").getAsJsonObject().addProperty("from", fromModuleName);
        } catch (NullPointerException e){
            chain.getProcessObject().addProperty("notification", "request missing header");
            return false;
        }
        // check the parameters on the header
        if (!chain.getProcessObject().get("header").getAsJsonObject().has("to")) {
            chain.getProcessObject().addProperty("notification",  "cannot find the 'to' filed on the header");
            return false;
        }
        if (!chain.getProcessObject().get("header").getAsJsonObject().has("clientId")){
            chain.getProcessObject().addProperty("notification",  "cannot find the 'clientId' field on the header");
            return false;
        }
        if (!chain.getProcessObject().get("header").getAsJsonObject().has("status")) {
            chain.getProcessObject().addProperty("notification",  "cannot find the 'status' on the header");
            return false;
        }
        if (!chain.getProcessObject().has("body")) {
            chain.getProcessObject().addProperty("notification",  "the body of the request does not exists");
            return false;
        }
        return true;
    }
}
