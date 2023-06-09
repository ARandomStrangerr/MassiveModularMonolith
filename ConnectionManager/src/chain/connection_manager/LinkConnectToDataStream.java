package chain.connection_manager;

import chain.Chain;
import memorable.ConnectionManager;
import socket_handler.connection_manager.SocketHandler;
import socket_handler.SocketWrapper;

class LinkConnectToDataStream extends chain.LinkConnectToDataStream {
    LinkConnectToDataStream(Chain chain) {
        super(chain);
    }

	@Override
	protected void saveSocket(SocketWrapper socket) {
		ConnectionManager.socketToDataStream = socket;
	}

	@Override
	protected Runnable getSocketHandler(SocketWrapper socket) {
		return new SocketHandler(socket, chain.getProcessObject().get("moduleName").getAsString());
	}

}
