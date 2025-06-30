package org.example.eventsystem.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    public static void sendEmail(String to, String subject, String messageText) {
        // Sender's email ID and credentials
        final String from = "24mcab44@kristujayanti.com"; // ✅ Replace with your actual email
        final String password = "mbrw rwms dubc fblf";       // ✅ Replace with your app-specific password

        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Get the Session object
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header
            message.setFrom(new InternetAddress(from));

            // Set To: header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header and message body
            message.setSubject(subject);
            message.setText(messageText);

            // Send message
            Transport.send(message);
            System.out.println("📧 Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
