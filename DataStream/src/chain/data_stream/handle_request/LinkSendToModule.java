package chain.data_stream.handle_request;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.DataStream;
import socket_handler.SocketWrapper;

import java.io.IOException;

class LinkSendToModule extends Link {
    LinkSendToModule(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        // get header of the processing request
        JsonObject header = chain.getProcessObject().get("header").getAsJsonObject();
        // get the socket which the end module associated to
        SocketWrapper toSocket = DataStream.getInstance().getListener().getSocket(header.get("to").getAsString());
        if (toSocket == null) {
            chain.getProcessObject().addProperty("notification", "No socket associated with the name: " + header.get("to").getAsString());
            chain.getProcessObject().addProperty("error", "Vui lòng liên lạc với công ty phầm mềm để được giải quyết");
            chain.getProcessObject().getAsJsonObject().get("header").getAsJsonObject().addProperty("status", false);
            return false;
        }
        // write the message to the module
        try {
            toSocket.write(chain.getProcessObject().toString());
        } catch (IOException e) {
            System.err.println("Cannot write to the socket");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
