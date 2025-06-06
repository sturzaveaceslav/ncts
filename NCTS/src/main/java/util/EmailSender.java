package md.ncts.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailSender {

    public static void sendEmailOutlook(String toEmail, File attachment) throws Exception {
        final String fromEmail = "slavunea90@gmail.com";
        final String password = "foia jkxi whud plbg"; // vezi notă mai jos

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Fișier JSON generat automat din NCTS");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Salut,\n\nAi primit atașat fișierul JSON generat automat din aplicația NCTS.");

        MimeBodyPart filePart = new MimeBodyPart();
        filePart.attachFile(attachment);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(filePart);

        message.setContent(multipart);

        Transport.send(message);
        System.out.println("✅ Email trimis cu succes către: " + toEmail);
    }
}
