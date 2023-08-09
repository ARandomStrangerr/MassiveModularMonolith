package chain.public_fianace;

import chain.Chain;
import memorable.PublicFinance;
import socket_handler.SocketWrapper;
import socket_handler.public_finance.SocketHandler;

class LinkConnectToDataStream extends chain.LinkConnectToDataStream {
	LinkConnectToDataStream(Chain chain) {
		super(chain);
	}

	@Override
	protected void saveSocket(SocketWrapper socket) {
		PublicFinance.socketToDataStream = socket;
	}

	@Override
	protected Runnable getSocketHandler(SocketWrapper socket) {
		return new SocketHandler(socket, chain.getProcessObject().get("moduleName").getAsString());
	}
}
