package memorable;

public class SMTPMail {
    private String moduleName;
    private static SMTPMail instance;

    public static SMTPMail getInstance() {
        if (instance == null) instance = new SMTPMail();
        return instance;
    }

    private SMTPMail(){}

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
