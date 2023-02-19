package chain.data_stream.handle_request;

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
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("from", fromModuleName);
        // check the parameters on the header
        if (!chain.getProcessObject().get("header").getAsJsonObject().has("to")) {
            System.err.println("cannot find the 'to' filed on the header");
            return false;
        }
        if (!chain.getProcessObject().get("header").getAsJsonObject().has("clientId")){
            System.err.println("cannot find the 'clientId' field on the header");
            return false;
        }
        if (!chain.getProcessObject().get("header").getAsJsonObject().has("status")) {
            System.err.println("cannot find the 'status' on the header");
            return false;
        }
        if (!chain.getProcessObject().has("body")) {
            System.err.println("the body of the request does not exists");
            return false;
        }
        return true;
    }
}
