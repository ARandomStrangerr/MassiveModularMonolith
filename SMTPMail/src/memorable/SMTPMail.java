package memorable;

import socket_handler.SocketWrapper;

public class SMTPMail {
    public String moduleName;
    public SocketWrapper socket;
    private static SMTPMail instance;

    public static SMTPMail getInstance() {
        if (instance == null) instance = new SMTPMail();
        return instance;
    }
}
