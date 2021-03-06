package com.trung.wayup.WayUp.util;

import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.Properties;

public class SendMail {

    public static void send(String to){
        try{
            String host ="smtp.gmail.com" ;
            String user = "lequangtrungkhmt4@gmail.com";
            String pass = "trungthuychienthang62";
            String from = "lequangtrungkhmt4@gmail.com";
            String subject = "Xác nhận ứng tuyển";
            String messageText = "Chúng tôi đã nhận được thông tin ứng tuyển của bạn, chúng tôi sẽ hồi đáp bạn trong thời gian sớm nhất.";
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject); msg.setSentDate(new Date());
            msg.setText(messageText);

            Transport transport=mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message send successfully");
        }catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

}
