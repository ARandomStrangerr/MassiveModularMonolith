package chain.viettel_einvoice;

import chain.Chain;
import memorable.ViettelEInvoice;
import socket_hander.viettel_einvoice.SocketHandler;
import socket_handler.SocketWrapper;

public class LinkConnectToDataStream extends chain.LinkConnectToDataStream {
    public LinkConnectToDataStream(Chain chain) {
        super(chain);
    }

	@Override
	protected void saveSocket(SocketWrapper socket) {
		ViettelEInvoice.socketToDataStream = socket;
	}

	@Override
	protected Runnable getSocketHandler(SocketWrapper socket) {
		return new SocketHandler(socket, chain.getProcessObject().get("moduleName").getAsString());
	}
}
