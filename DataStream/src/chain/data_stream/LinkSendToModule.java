package chain.data_stream;

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
            String msg = String.format("Dịch vụ %s hiện tại không hoạt động", chain.getProcessObject().get("header").getAsJsonObject().get("to").getAsString());
            chain.getProcessObject().addProperty("error", msg);
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
