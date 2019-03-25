package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.Notification;
import com.trung.wayup.WayUp.repository.NotificationRepository;
import com.trung.wayup.WayUp.service.base.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification insert(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void delete(int id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Collection<Notification> gettAll() {
        return (Collection<Notification>) notificationRepository.findAll();
    }

    @Override
    public List<Notification> findAllByEmail(String email) {
        return notificationRepository.findAllByEmail(email);
    }

    @Override
    public Notification findNotificationByEmailAndSkill(String email, String skill) {
        return notificationRepository.findNotificationByEmailAndSkill(email, skill);
    }
}
