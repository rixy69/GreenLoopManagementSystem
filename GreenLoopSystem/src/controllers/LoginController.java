package controllers;

import models.DashboardModel;
import models.Role;
import services.EmployeeService;
import models.Employee;
import services.RoleService;
import views.DashboardView;
import views.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private EmployeeService employeeService;
    private RoleService roleService;
    private Employee employee;



    public LoginController(Employee employee, LoginView loginView, EmployeeService employeeService, RoleService roleService) {
        this.loginView = loginView;
        this.employeeService = employeeService;
        this.roleService = roleService;

        this.loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        this.loginView.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
    }

    private void handleLogin() {
        try {
            String username = loginView.getUsernameText().getText();
            String password = new String(loginView.getPasswordText().getPassword());

            // Validate input
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginView, "Please enter both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Attempting login with username: " + username);
            
            employee = employeeService.authenticate(new Employee(username, password));

            if (employee != null && employee.getEmployeeId() > 0) {
                System.out.println("Login successful! Employee ID: " + employee.getEmployeeId());
                System.out.println("Loading role for role ID: " + employee.getRoleId());
                
                employee.setRole(roleService.getRoleById(employee.getRoleId()));
                
                System.out.println("Creating dashboard...");
                DashboardModel dashboardModel = new DashboardModel(employee);
                DashboardView dashboardView = new DashboardView(dashboardModel);
                DashboardController dashboardController = new DashboardController(dashboardView, dashboardModel);
                
                System.out.println("Showing dashboard...");
                dashboardView.setVisible(true);
                this.loginView.setVisible(false);
                this.loginView.dispose();
                System.out.println("Dashboard displayed successfully!");
            } else {
                System.out.println("Authentication failed. Employee ID: " + (employee != null ? employee.getEmployeeId() : "null"));
                JOptionPane.showMessageDialog(loginView, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "An error occurred during login: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        loginView.getUsernameText().setText("");
        loginView.getPasswordText().setText("");
    }
}
