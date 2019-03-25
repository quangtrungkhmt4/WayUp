package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findAllByEmail(String email);

    Notification findNotificationByEmailAndSkill(String email, String skill);
}
