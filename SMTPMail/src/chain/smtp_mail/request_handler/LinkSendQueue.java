package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.SMTPMail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class LinkSendQueue extends Link {
    private static final Hashtable<String, LinkedList<JsonObject>> queueTable = new Hashtable<>();

    public LinkSendQueue(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String key = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
        LinkedList<JsonObject> lst;
        if (LinkSendQueue.queueTable.containsKey(key)) {
            lst = LinkSendQueue.queueTable.get(key);
        } else {
            lst = new LinkedList<>();
            LinkSendQueue.queueTable.put(key, lst);
            new Thread(sendMailRunnable(key)).start();
        }
        synchronized (lst) {
            lst.add(chain.getProcessObject());
        }
        return true;
    }

    private Runnable sendMailRunnable(String emailAddress) {
        return () -> {
            int i = 0;
            while (i < 10) {
                JsonObject processObject;
                try {
                    synchronized (queueTable) { // retreat the information of a json object which use to send the mail
                        processObject = queueTable.get(emailAddress).removeFirst();
                    }
                } catch (NullPointerException | NoSuchElementException e) {
                    i++;
                    try {
                        Thread.sleep(4000); // wait for 4 seconds before sending each mail to space out evenly
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        continue;
                    }
                    continue;
                }

                JsonObject body = processObject.get("body").getAsJsonObject();
                // create configuration
                Properties prop = new Properties();
                prop.put("mail.smtp.auth", true);
                prop.put("mail.smtp.starttls.enable", true);
                try {
                    prop.put("mail.smtp.host", body.getAsJsonObject().get("host").getAsString());
                } catch (NullPointerException e) {
                    System.err.println("Address to the host SMTP is missing");
                    sendUpdate(processObject, "error", "Địa chỉ server SMTP bị bỏ trống");
                    continue;
                }
                try {
                    prop.put("mail.smtp.port", body.getAsJsonObject().get("port").getAsInt());
                } catch (NullPointerException e) {
                    System.err.println("Port the the host SMTP is missing");
                    sendUpdate(processObject, "error", "Cổng server SMTP bị bỏ trống");
                    continue;
                }
                // create session
                String username, password;
                try {
                    username = body.getAsJsonObject().get("username").getAsString();
                } catch (NullPointerException e) {
                    System.err.println("username is missing");
                    sendUpdate(processObject, "error", "Địa chỉ e-mail người gửi thư bị bỏ trống");
                    continue;
                }
                try {
                    password = body.getAsJsonObject().get("password").getAsString();
                } catch (NullPointerException e) {
                    System.err.println("password is missing");
                    sendUpdate(processObject, "error", "Mật khẩu e-mail người gửi thư bị bỏ trống");
                    continue;
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
                    sendUpdate(processObject, "error", "địa chỉ người gửi không hợp lệ");
                    System.err.println("Email format is not correct");
                    e.printStackTrace();
                    continue;
                }
                try {
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(body.getAsJsonObject().get("recipient").getAsString())); // set the recipient mail
                } catch (NullPointerException e) {
                    sendUpdate(processObject, "error", "Địa chỉ người nhận bỏ trống");
                    System.err.println("recipient is missing");
                    continue;
                } catch (MessagingException e) {
                    sendUpdate(processObject, "error", "something went wrong");
                    e.printStackTrace();
                    continue;
                }
                try {
                    message.setSubject(body.get("subject").getAsString()); // set subject of the mail
                } catch (NullPointerException e) {
                    sendUpdate(processObject, "error", "tiêu đề thư chưa được điền");
                    System.err.println("Subject of the mail is missing");
                    continue;
                } catch (MessagingException e) {
                    sendUpdate(processObject, "error", "something went wrong");
                    e.printStackTrace();
                    continue;
                }
                // create mail body
                MimeBodyPart bodyPart = new MimeBodyPart();

                try {
                    bodyPart.setContent(body.get("message").getAsString(), "text/html; charset=utf-8");
                } catch (MessagingException e) {
                    e.printStackTrace();
                    continue;
                } catch (NullPointerException e) {
                    System.err.println("Body of the mail is missing");
                    sendUpdate(processObject, "error", "nội dung thư bị bỏ trống");
                    continue;
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
                // create attachment
                if (body.has("attachment") && body.get("attachment").getAsJsonArray().size() != 0) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    for (JsonElement attachment : body.get("attachment").getAsJsonArray()) {
                        JsonObject attachmentObj = attachment.getAsJsonObject();
                        byte[] decodedBase64 = Base64.getDecoder().decode(attachmentObj.get("blob").getAsString());
                        try (FileOutputStream os = new FileOutputStream(attachmentObj.get("name").getAsString())) {
                            os.write(decodedBase64);
                            attachmentPart.attachFile(attachmentObj.get("name").getAsString());
                        } catch (IOException | MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        multipart.addBodyPart(attachmentPart);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
                // send the mail
//                try {
//                    Transport.send(message);
//                } catch (MessagingException e) {
//                    sendUpdate(processObject, "error", e.getMessage());
//                    e.printStackTrace();
//                    continue;
//                }
                // todo remove file after done using
                System.out.printf("Success send mail to %s \n", body.getAsJsonObject().get("recipient").getAsString());
                sendUpdate(processObject, "update", String.format("Thành công gởi thư đến địa chỉ %s", body.get("recipient").getAsString()));
            }
            synchronized (queueTable) {
                queueTable.remove(emailAddress); // remove the instance from the table after done
            }
        };
    }

    private void sendUpdate(JsonObject processObject, String property, String propertyValue) {
        JsonObject newBody = new JsonObject();
        newBody.addProperty(property, propertyValue);
        processObject.add("body", newBody);
        processObject.get("header").getAsJsonObject().add("to", processObject.get("header").getAsJsonObject().remove("from"));
        try {
            SMTPMail.getInstance().socket.write(processObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
