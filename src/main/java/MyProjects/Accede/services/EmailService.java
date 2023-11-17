
package MyProjects.Accede.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    public JavaMailSender emailSender;

    public EmailService() {
    }

    public void sendMessage(String to, String subject, String text) {
        try {
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            String mailcontent = "Hello,\n\n \n \tYou recently tried to log into Accede. In order to complete your login, please use the code below\n\n" + text + " \t\nIf this wasn't you, please change your password.\n\nThank you,\nAccede";
            helper.setFrom("AccedeProject@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailcontent);
            this.emailSender.send(message);
        } catch (MessagingException var7) {
            log.error("Mail sending failed: {}", var7.getMessage());
        }

    }
}