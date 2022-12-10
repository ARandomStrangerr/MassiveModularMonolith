package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;

import java.io.IOException;

public class LinkSendRequest extends Link {
    public LinkSendRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try {
            ConnectionManager.getInstance().socket.write(chain.getProcessObject().toString());
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
