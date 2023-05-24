package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import memorable.ViettelEInvoice;

import java.io.IOException;

public class LinkReturnRequest extends Link {
    public LinkReturnRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
		try {
			Thread.sleep(2000);
		} catch (Exception ignore){}
        try{
            ViettelEInvoice.socketToDataStream.write(chain.getProcessObject().toString());
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
