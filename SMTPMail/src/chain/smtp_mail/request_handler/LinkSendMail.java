package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.mail.util.MailConnectException;

import javax.mail.*;
import javax.mail.internet.*;
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
            chain.getProcessObject().addProperty("error", "Địa chỉ server bị bỏ trống");
            System.err.println("Address to the host SMTP is missing");
            return false;
        }
        try {
            prop.put("mail.smtp.port", body.getAsJsonObject().get("port").getAsInt());
        } catch (NullPointerException e) {
            chain.getProcessObject().addProperty("error", "cổng nhận thông tin của server bị bỏ trống");
            System.err.println("Port the the host SMTP is missing");
            return false;
        }
        // create session
        String username, password;
        try {
            username = body.getAsJsonObject().get("username").getAsString();
        } catch (NullPointerException e) {
            chain.getProcessObject().addProperty("error", "tên người dùng bị bỏ trống");
            System.err.println("username is missing");
            return false;
        }
        try {
            password = body.getAsJsonObject().get("password").getAsString();
        } catch (NullPointerException e) {
            chain.getProcessObject().addProperty("error", "mật khẩu bị bỏ trống");
            System.err.println("password is missing");
            return false;
        }
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
        }
        try {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(body.getAsJsonObject().get("recipient").getAsString())); // set the recipient mail
        }catch (NullPointerException e){
            chain.getProcessObject().addProperty("error", "Địa chỉ người nhận bỏ trống");
            System.err.println("recipient is missing");
            return false;
        } catch (MessagingException e) {
            chain.getProcessObject().addProperty("error", "something went wrong");
            e.printStackTrace();
            return false;
        }
        try {
            message.setSubject(body.get("subject").getAsString()); // set subject of the mail
        }catch (NullPointerException e){
            chain.getProcessObject().addProperty("error", "tiêu đề thư chưa được điền");
            System.err.println("Subject of the mail is missing");
            return false;
        }catch (MessagingException e) {
            chain.getProcessObject().addProperty("error", "something went wrong");
            e.printStackTrace();
            return false;
        }
        // create mail body
        MimeBodyPart bodyPart = new MimeBodyPart();

        try {
            bodyPart.setContent(body.get("message").getAsString(), "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        MimeMultipart multipart = new MimeMultipart();
        try {
            multipart.addBodyPart(bodyPart);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            message.setContent(multipart);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        // todo create attachment
        // send the mail
        try {
            Transport.send(message);
        } catch (MessagingException e){
            chain.getProcessObject().addProperty("error", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
