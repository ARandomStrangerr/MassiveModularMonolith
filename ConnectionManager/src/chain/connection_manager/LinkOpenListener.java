package chain.connection_manager;

import chain.Chain;
import memorable.ConnectionManager;
import socket_handler.ListenerHandler;
import socket_handler.ListenerWrapper;

class LinkOpenListener extends chain.LinkOpenListener {
	LinkOpenListener(Chain chain) {
		super(chain);
	}

	@Override
	protected void saveListener(ListenerWrapper listener) {
		ConnectionManager.listener = listener;
	}

	@Override
	protected ListenerHandler getListenerHandler(ListenerWrapper listener) {
		return new socket_handler.connection_manager.ListenerHandler(listener, chain.getProcessObject().get("timeout").getAsInt());
	}
}
