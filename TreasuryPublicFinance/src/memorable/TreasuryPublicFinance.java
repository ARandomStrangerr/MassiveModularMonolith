package memorable;

import socket_handler.SocketWrapper;

import java.util.HashMap;
import java.util.LinkedList;

public class TreasuryPublicFinance {
    private static TreasuryPublicFinance instance;

    public static TreasuryPublicFinance getInstance() {
        if (instance == null) instance = new TreasuryPublicFinance();
        return instance;
    }

    public String name;
    public SocketWrapper socketToDataStream;
    public HashMap<String, LinkedList<String>> apiStructure;
}
