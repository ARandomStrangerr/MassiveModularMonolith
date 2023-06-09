package chain.connection_manager;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;

import java.io.IOException;

public class LinkSendRequestToDataSteam extends Link {
    public LinkSendRequestToDataSteam(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try {
            ConnectionManager.socketToDataStream.write(chain.getProcessObject().toString());
        } catch (IOException e){
            chain.getProcessObject().addProperty("error", "Không thể chuyển dữ liệu đến Stream");
            return false;
        }
        return true;
    }
}
