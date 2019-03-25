package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Notification;

import java.util.List;

public interface NotificationService extends Service<Notification> {
    List<Notification> findAllByEmail(String email);
    Notification findNotificationByEmailAndSkill(String email, String skill);
}
