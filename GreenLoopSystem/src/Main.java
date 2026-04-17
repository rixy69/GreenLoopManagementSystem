import controllers.LoginController;
import java.sql.Connection;
import javax.swing.*;
import services.DatabaseConnectionService;
import services.EmployeeService;
import services.Impl.EmployeeServiceImpl;
import services.Impl.MySQLConnectionService;
import services.Impl.RoleServiceImpl;
import services.RoleService;
import views.LoginView;

public class Main {

    public static void main(String[] args) {

        DatabaseConnectionService databaseConnectionService = new MySQLConnectionService();

        // Test database connection
        try (Connection testConn = databaseConnectionService.getConnection()) {
            // Connection successful
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage() + "\n\nPlease ensure MySQL is running, the 'greenloop' database exists, and the classpath includes the MySQL connector JAR.", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Create the view
        LoginView loginView = new LoginView();
        loginView.setVisible(true);

        // Create the service
        EmployeeService employeeService = new EmployeeServiceImpl(databaseConnectionService);
        RoleService roleService = new RoleServiceImpl(databaseConnectionService);
        // Create the controller
        LoginController loginController = new LoginController(null, loginView, employeeService, roleService);
    }

    /*public static void main(String[] args) {
        DatabaseConnectionService databaseConnectionService = new MySQLConnectionService();
        EmployeeService employeeService = new EmployeeServiceImpl(databaseConnectionService);
        RoleService roleService = new RoleServiceImpl(databaseConnectionService);
        Employee employee = employeeService.authenticate(new Employee("john", "john"));
        if(employee.getEmployeeId()>0){
            employee.setRole(roleService.getRoleById(employee.getRoleId()));
            DashboardModel dashboardModel = new DashboardModel(employee);
            DashboardView dashboardView = new DashboardView(dashboardModel);
            DashboardController dashboardController = new DashboardController(dashboardView, dashboardModel);
            dashboardView.setVisible(true);
        }else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/

}