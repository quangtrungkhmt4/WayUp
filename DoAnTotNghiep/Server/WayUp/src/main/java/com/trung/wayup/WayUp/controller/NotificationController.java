package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Notification;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.JobResponse;
import com.trung.wayup.WayUp.service.base.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class NotificationController extends AbstractController{

    private NotificationService notificationService;

    @RequestMapping(method = RequestMethod.POST, value = "/noties")
    public ResponseEntity<Response> insertJob(@RequestBody Notification noti){
        Notification isExists = notificationService.findNotificationByEmailAndSkill(noti.getEmail(), noti.getSkill());
        if (isExists == null){
            Notification notification = notificationService.insert(noti);
            return responseData(notification==null?new BooleanResponse(false):new BooleanResponse(true));
        }else {
            return responseData(new BooleanResponse(true));
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/noties")
    public ResponseEntity<Response> deleteNoti(@RequestParam("email") String email){
        List<Notification> notifications = notificationService.findAllByEmail(email);
        for (Notification n : notifications){
            notificationService.delete(n.getId_noti());
        }
        BooleanResponse objectResponse = new BooleanResponse(true);
        return responseData(objectResponse);
    }
}
