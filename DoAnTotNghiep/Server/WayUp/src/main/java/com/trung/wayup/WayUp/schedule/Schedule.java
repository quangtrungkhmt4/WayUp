package com.trung.wayup.WayUp.schedule;

import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.model.Notification;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.service.base.ApplyProfileService;
import com.trung.wayup.WayUp.service.base.JobService;
import com.trung.wayup.WayUp.service.base.NotificationService;
import com.trung.wayup.WayUp.util.DateTime;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
@AllArgsConstructor
public class Schedule {
    private static final long TIME = 43200000;

    @Autowired
    public JavaMailSender emailSender;
    private NotificationService notificationService;
    private JobService jobService;

    @Scheduled(fixedRate = TIME)
    public void sendMail() {


        List<Notification> noties = (List<Notification>) notificationService.gettAll();

        for (Notification noti : noties){

            List<Job> jobs = jobService.searchJobWithSkillAndJoinDate(noti.getSkill()
                    , DateTime.convertDate(DateTime.currentDateTime()));

            String content = "<h1>Danh sách tin tuyển dụng về " + noti.getSkill()
                    + " trong ngày " + DateTime.convertDate(DateTime.currentDateTime()) + "</h1>";

            if (jobs.size() > 0){
                for (int i=0; i< jobs.size(); i++){
                    content = content + setTagH3(jobs.get(i).getTitle());
                }
                System.out.println(content);

                try {
                    MimeMessage message = emailSender.createMimeMessage();
                    boolean multipart = true;
                    MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
                    message.setContent(content, "text/html; charset=utf-8");
                    helper.setTo(noti.getEmail());
                    helper.setSubject("Thông tin tuyển dụng");
                    this.emailSender.send(message);
                }catch (Exception e){

                }
            }
        }

    }

    private String setTagH3(String content){
        return "<h3>" + content + "</h3>";
    }

}
