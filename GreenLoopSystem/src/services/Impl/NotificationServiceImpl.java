package services.Impl;

import models.Notification;
import services.DatabaseConnectionService;
import services.NotificationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private DatabaseConnectionService connectionService;

    public NotificationServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addNotification(Notification notification) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Notifications (part_id, part_name, min_quantity, notify) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, notification.getPartId());
            statement.setString(2, notification.getPartName());
            statement.setInt(3, notification.getMinQuantity());
            statement.setBoolean(4, notification.isNotify());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification getNotificationById(int notificationId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select n_i.notification_id as notification_id,\n" +
                             "       n_i.part_id         as part_id,\n" +
                             "       n_i.part_name       as part_name,\n" +
                             "       sum(n_i.quantity)   as remaining_quantity,\n" +
                             "       n_i.min_quantity    as min_quantity,\n" +
                             "       n_i.notify          as notify\n" +
                             "from (SELECT n.*, i.quantity\n" +
                             "      FROM notifications n\n" +
                             "               LEFT JOIN inventory i on n.part_id = i.part_id) as n_i\n" +
                             "group by n_i.part_id, n_i.notification_id having notification_id=?")) {
            statement.setInt(1, notificationId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getNotificationFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Notification getNotificationByPartId(int partId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select n_i.notification_id as notification_id,\n" +
                             "       n_i.part_id         as part_id,\n" +
                             "       n_i.part_name       as part_name,\n" +
                             "       sum(n_i.quantity)   as remaining_quantity,\n" +
                             "       n_i.min_quantity    as min_quantity,\n" +
                             "       n_i.notify          as notify\n" +
                             "from (SELECT n.*, i.quantity\n" +
                             "      FROM notifications n\n" +
                             "               LEFT JOIN inventory i on n.part_id = i.part_id) as n_i\n" +
                             "group by n_i.part_id, n_i.notification_id having part_id=?")) {
            statement.setInt(1, partId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getNotificationFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select n_i.notification_id as notification_id,\n" +
                             "       n_i.part_id         as part_id,\n" +
                             "       n_i.part_name       as part_name,\n" +
                             "       sum(n_i.quantity)   as remaining_quantity,\n" +
                             "       n_i.min_quantity    as min_quantity,\n" +
                             "       n_i.notify          as notify\n" +
                             "from (SELECT n.*, i.quantity\n" +
                             "      FROM notifications n\n" +
                             "               LEFT JOIN inventory i on n.part_id = i.part_id) as n_i\n" +
                             "group by n_i.part_id, n_i.notification_id");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                notifications.add(getNotificationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    @Override
    public boolean updateNotification(Notification notification) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Notifications SET part_id = ?, part_name = ?, min_quantity = ?, notify = ? WHERE notification_id = ?")) {
            statement.setInt(1, notification.getPartId());
            statement.setString(2, notification.getPartName());
            statement.setInt(3, notification.getMinQuantity());
            statement.setBoolean(4, notification.isNotify());
            statement.setInt(5, notification.getNotificationId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteNotification(int notificationId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Notifications WHERE notification_id = ?")) {
            statement.setInt(1, notificationId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Notification getNotificationFromResultSet(ResultSet resultSet) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(resultSet.getInt("notification_id"));
        notification.setPartId(resultSet.getInt("part_id"));
        notification.setPartName(resultSet.getString("part_name"));
        notification.setRemainingQuantity(resultSet.getInt("remaining_quantity"));
        notification.setMinQuantity(resultSet.getInt("min_quantity"));
        notification.setNotify(resultSet.getBoolean("notify"));
        return notification;
    }
}
