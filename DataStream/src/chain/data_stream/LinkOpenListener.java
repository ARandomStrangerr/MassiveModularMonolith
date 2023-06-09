package chain.data_stream;

import chain.Chain;
import memorable.DataStream;
import socket_handler.ListenerHandler;
import socket_handler.ListenerWrapper;

class LinkOpenListener extends chain.LinkOpenListener {
	LinkOpenListener(Chain chain) {
		super(chain);
	}

	@Override
	protected void saveListener(ListenerWrapper listener) {
		DataStream.getInstance().setListener(listener);
	}

	@Override
	protected ListenerHandler getListenerHandler(ListenerWrapper listener) {
		return new socket_handler.data_stream.ListenerHandler(listener, chain.getProcessObject().get("timeout").getAsInt());
	}
}
