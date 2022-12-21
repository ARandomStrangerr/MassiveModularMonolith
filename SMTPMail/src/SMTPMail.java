import chain.smtp_mail.start_module.ChainStartModule;
import com.google.gson.JsonObject;

public class SMTPMail {
    public static void main(String[] args){
        JsonObject startJson = new JsonObject();
        startJson.addProperty("address", "localhost");
        startJson.addProperty("port", 9999);
        startJson.addProperty("keyStorePath","key.jks");
        startJson.addProperty("keyStorePassword", "hungcom23");
        startJson.addProperty("moduleName", "SMTPMail");
        if (!new ChainStartModule(startJson).execute()) System.exit(-1);
    }
}
