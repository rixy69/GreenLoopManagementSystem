package models;

public class Employee {
    private int employeeId;
    private String title;
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private String mobile;
    private String email;
    private String password;
    private String schedule;
    private int roleId;
    private Role role;

    public Employee() {
    }

    // Constructor
    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void clone_(Employee employee) {
        this.employeeId = employee.employeeId;
        this.title = employee.title;
        this.firstName = employee.firstName;
        this.lastName = employee.lastName;
        this.username = employee.username;
        this.address = employee.address;
        this.mobile = employee.mobile;
        this.email = employee.email;
        this.password = employee.password;
        this.schedule = employee.schedule;
        this.roleId = employee.roleId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "\n\temployeeId=" + employeeId +
                ",\n\ttitle='" + title + '\'' +
                ",\n\tfirstName='" + firstName + '\'' +
                ",\n\tlastName='" + lastName + '\'' +
                ",\n\tusername='" + username + '\'' +
                ",\n\taddress='" + address + '\'' +
                ",\n\tmobile='" + mobile + '\'' +
                ",\n\temail='" + email + '\'' +
                ",\n\tpassword='" + password + '\'' +
                ",\n\tschedule='" + schedule + '\'' +
                ",\n\troleId=" + roleId +
                "\n}";
    }
}
