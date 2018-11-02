package springmvc.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import springmvc.demo.services.email.SmtpMailSender;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private static SmtpMailSender smtpMailSender;

    public EmailService(SmtpMailSender mailSender) {
        this.smtpMailSender = mailSender;
    }

    public static void send(String to, String subject, String body) throws MessagingException {
        smtpMailSender.send(to, subject, body);
    }


}
