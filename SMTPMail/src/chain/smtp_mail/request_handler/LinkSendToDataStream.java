package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import memorable.SMTPMail;

import java.io.IOException;

public class LinkSendToDataStream extends Link {
    public LinkSendToDataStream(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try {
            SMTPMail.getInstance().socket.write(chain.getProcessObject().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
