package memorable;

import socket_handler.ListenerWrapper;

public class DataStream {
    private ListenerWrapper listener;
    private String moduleName;
    private static DataStream instance;

    public static DataStream getInstance() {
        if (instance == null) instance = new DataStream();
        return instance;
    }

    private DataStream(){}

    public ListenerWrapper getListener() {
        return listener;
    }

    public void setListener(ListenerWrapper listener) {
        this.listener = listener;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
