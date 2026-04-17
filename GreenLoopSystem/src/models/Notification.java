package models;

public class Notification {
    private int notificationId;
    private int partId;
    private String partName;
    private int remainingQuantity;
    private int minQuantity;
    private boolean notify;

    public Notification() {}


    public Notification(int notificationId, int partId, String partName, int remainingQuantity, int minQuantity, boolean notify) {
        this.notificationId = notificationId;
        this.partId = partId;
        this.partName = partName;
        this.remainingQuantity = remainingQuantity;
        this.minQuantity = minQuantity;
        this.notify = notify;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", partId=" + partId +
                ", partName='" + partName + '\'' +
                ", remainingQuantity=" + remainingQuantity +
                ", minQuantity=" + minQuantity +
                ", notify=" + notify +
                '}';
    }
}
