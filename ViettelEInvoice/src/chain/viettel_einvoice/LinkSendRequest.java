package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import memorable.ViettelEInvoice;

import java.io.IOException;

public class LinkSendRequest extends Link {
    public LinkSendRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try{
            ViettelEInvoice.socketToDataStream.write(chain.getProcessObject().toString());
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
