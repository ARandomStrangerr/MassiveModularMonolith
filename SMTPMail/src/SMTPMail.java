import chain.smtp_mail.start_module.ChainStartModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SMTPMail {
    public static void main(String[] args){
        if (args.length == 0){
            System.err.println("Missing the argument");
            return;
        }
        JsonObject information = new Gson().fromJson(args[0], JsonObject.class);
        if (!new ChainStartModule(information).execute()) System.exit(2);
    }
}
