package chain.gmail.handle_request;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.GMail;

import java.io.IOException;

class LinkMakeAuthorizationParameter extends Link {
    public LinkMakeAuthorizationParameter(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        // obtain server socket to listen to request

        // create authorization parameter
        String clientId = GMail.getInstance().clientId,
            redirectUri = GMail.getInstance().redirectUri,
            scope = "https://www.googleapis.com/auth/gmail.insert",
            responseType = "code",
            authorizationUrl = String.format("https://acocunts.google.com/o/oauth2/v2.auth?client_id=%s&redirect_url=%s$response_type=%s&scope=%s", clientId, redirectUri, responseType, scope);
        // send the authorization parameter to client
        JsonObject authorizationInfo = new JsonObject(),
            header = new JsonObject(),
            body = new JsonObject();
        header.addProperty("to", "ConnectionManager");
        header.add("clientId", chain.getProcessObject().get("header").getAsJsonObject().get("clientId"));
        header.addProperty("status", true);
        body.addProperty("url", authorizationUrl);
        authorizationInfo.add("header", header);
        authorizationInfo.add("body", body);
        try {
            GMail.getInstance().socketToDataStream.write(authorizationInfo.getAsString());
        }catch (IOException e){
            e.printStackTrace();
        }
        // wait for the parameter at indicated port
        return false;
    }
}
