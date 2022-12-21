package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

class LinkSendMail extends Link {
    LinkSendMail(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject body = chain.getProcessObject().get("body").getAsJsonObject();
        // create configuration
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        try {
            prop.put("mail.smtp.host", body.getAsJsonObject().get("host").getAsString());
        } catch (NullPointerException e) {
            chain.getProcessObject().addProperty("error", "địa chỉ server bị bỏ trống");
            System.err.println("Address to the host SMTP is missing");
            e.printStackTrace();
            return false;
        }
        try {
            prop.put("mail.smtp.port", body.getAsJsonObject().get("port").getAsInt());
        } catch (NullPointerException e) {
            chain.getProcessObject().addProperty("error", "cổng nhận thông tin của server bị bỏ trống");
            System.err.println("Port the the host SMTP is missing");
            e.printStackTrace();
            return false;
        }
        // create session
        String username, password;
        username = body.getAsJsonObject().get("username").getAsString();
        password = body.getAsJsonObject().get("password").getAsString();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // create mail header
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username)); // mail from whom
        } catch (MessagingException e) {
            chain.getProcessObject().addProperty("error", "địa chỉ người gửi không hợp lệ");
            System.err.println("Email format is not correct");
            e.printStackTrace();
            return false;
        }try{
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(body.getAsJsonObject().get("recipient").getAsString())); // set the recipient mail
        message.setSubject(body.get("subject").getAsString()); // set subject of the mail
        // create mail body
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body.get("message").getAsString(), "text/html; charset=utf-8");
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        message.setContent(multipart);
        // create attachment
        for (JsonElement file : body.get("files").getAsJsonArray()) {
            JsonObject fileObj = file.getAsJsonObject();
        }}catch (Exception e){}
        return true;
    }
}
