package services;

import models.Notification;

import java.util.List;

public interface NotificationService {
    boolean addNotification(Notification notification);
    Notification getNotificationById(int notificationId);
    Notification getNotificationByPartId(int partId);
    List<Notification> getAllNotifications();
    boolean updateNotification(Notification notification);
    boolean deleteNotification(int notificationId);
}
