package chain.treasury_public_finance.start_module;

import chain.Chain;
import chain.Link;
import memorable.TreasuryPublicFinance;
import socket_handler.SocketWrapper;
import socket_hander.treasurey_public_finace.SocketHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LinkCreateSocketToDataStream extends Link {
    public LinkCreateSocketToDataStream(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        InetAddress hostAddress;
        try {
            hostAddress = InetAddress.getByName(chain.getProcessObject().get("hostAddress").getAsString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        int hostPort = chain.getProcessObject().get("hostPort").getAsInt();
        String certPath = chain.getProcessObject().get("keyStorePath").getAsString(), certPassword = chain.getProcessObject().get("keyStorePassword").getAsString();
        SocketWrapper socket;
        try {
            socket = new SocketWrapper(hostAddress, hostPort, certPath, certPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        SocketHandler socketHandler = new SocketHandler(socket, TreasuryPublicFinance.getInstance().name);
        new Thread(socketHandler).start();
        System.out.printf("Connected to host at %s:%d\n", chain.getProcessObject().get("hostAddress").getAsString(), hostPort);
        return true;
    }
}
