package chapter_07.fixed.notifications;

import chapter_07.fixed.personnel.Employee;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

/*
now it will implement the EmployeeNotifier interface and the notify method is no longer static

this low level component now depends on an abstraction (EmployeeNotifier)
 */
public class EmailSender implements EmployeeNotifier {

    public void notify(Employee employee){
        // this method is no longer static

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "2525");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "5d205e8b9503f6",
                        "969159ed0125ea");
            }
        });

        // Send actual email
        try {
            Message message = new MimeMessage(session);

            // to/from
            message.setFrom(new InternetAddress("payment@globomantics.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(employee.getEmail()));

            // subject
            message.setSubject("Salary Notification");

            // content
            String msg = "Salary sent; Value: $" + employee.getMonthlyIncome();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            // send and wait (mail server has limit)
            Transport.send(message);
            System.out.println("Notified " + employee.getEmail());
            Thread.sleep(3000);
        }
        catch (MessagingException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
