package com.api.lista_de_compras.services.EmailService;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            System.out.println("Erro ao enviar email! " + e.getMessage());
            System.out.println(e);
            System.out.println(e.getCause());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        

        System.out.println("Email enviado com sucesso!");
    }
    
}
