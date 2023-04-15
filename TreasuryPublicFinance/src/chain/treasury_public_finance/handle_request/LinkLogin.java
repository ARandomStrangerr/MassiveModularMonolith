package chain.treasury_public_finance.handle_request;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import socket_handler.RESTRequest;

import java.io.IOException;

public class LinkLogin extends Link {
    public LinkLogin(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject sendObject = new JsonObject(),
            bodyObject = chain.getProcessObject().get("body").getAsJsonObject();
        sendObject.add("UserName", bodyObject.get("username"));
        sendObject.add("Password", bodyObject.get("password"));
        sendObject.add("BudgetCode", bodyObject.get("budgetCode"));
        sendObject.add("ComputerIP", bodyObject.get("ip"));
        sendObject.add("ComputerName", bodyObject.get("comName"));
        sendObject.add("Description", bodyObject.get("desc"));
        System.out.println(sendObject);
        try{
            System.out.println(RESTRequest.untrustPost(Route.LOGIN.route(),sendObject.toString(), null));
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
