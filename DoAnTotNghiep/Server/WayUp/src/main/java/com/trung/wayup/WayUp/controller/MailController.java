package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.JobResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class MailController extends AbstractController {

    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping(value = "/mail")
    public ResponseEntity<Response> sendSimpleMail(@RequestParam("mail") String mail){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setSubject("Xác nhận ứng tuyển");
            message.setText("Hồ sơ của bạn đã được gửi đi, vui lòng chờ phản hồi của bên tuyển dụng.");
            this.emailSender.send(message);
            return responseData(new BooleanResponse(true));
        }catch (Exception e){
            return responseData(new BooleanResponse(false));
        }
    }

    @RequestMapping(value = "/mail/sendCode")
    public ResponseEntity<Response> sendCodeMail(@RequestParam("mail") String mail, @RequestParam("code") int code){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setSubject("Xác nhận đăng ký đăng tin");
            message.setText("Mã code xác thực của bạn là: " + code);
            this.emailSender.send(message);
            return responseData(new BooleanResponse(true));
        }catch (Exception e){
            return responseData(new BooleanResponse(false));
        }
    }

    @RequestMapping(value = "/mail/sendPass")
    public ResponseEntity<Response> sendPassMail(@RequestParam("mail") String mail, @RequestParam("password") String pass){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setSubject("Quên mật khẩu");
            message.setText("Mật khẩu của bạn là: " + pass);
            this.emailSender.send(message);
            return responseData(new BooleanResponse(true));
        }catch (Exception e){
            return responseData(new BooleanResponse(false));
        }
    }
}
