package models;

public class DashboardModel {
    private Employee currentUser;

    public DashboardModel() {
    }

    public DashboardModel(Employee currentUser) {
        this.currentUser = currentUser;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser) {
        this.currentUser = currentUser;
    }
}
