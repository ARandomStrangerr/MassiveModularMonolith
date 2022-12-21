package chain.smtp_mail.start_module;

import chain.Chain;
import chain.Link;
import memorable.SMTPMail;

class LinkSetInfo extends Link {
    LinkSetInfo(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        SMTPMail.getInstance().moduleName = chain.getProcessObject().get("moduleName").getAsString();
        return false;
    }
}
