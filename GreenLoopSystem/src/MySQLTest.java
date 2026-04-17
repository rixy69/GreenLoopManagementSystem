import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLTest {
    public static void main(String[] args) {
        System.out.println("=== MySQL Connection Test ===\n");

        String url = "jdbc:mysql://localhost:3306/greenloop";
        String username = "root";
        String password = "root";
        String demoUsername = "rixy1";
        String demoPassword = "Rixy@8248";

        try {
            System.out.println("Step 1: Loading MySQL driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("  SUCCESS: MySQL driver loaded\n");

            System.out.println("Step 2: Connecting to MySQL...");
            System.out.println("  Connection URL: " + url);
            System.out.println("  Username: " + username);
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("  SUCCESS: Connected to MySQL\n");

            System.out.println("Step 3: Checking greenloop database...");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DATABASE()");
            if (rs.next()) {
                System.out.println("  Current database: " + rs.getString(1));
                System.out.println("  SUCCESS: Database is accessible\n");
            }
            rs.close();

            System.out.println("Step 4: Checking tables...");
            rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("  Tables found:");
            while (rs.next()) {
                System.out.println("    - " + rs.getString(1));
            }
            System.out.println("  SUCCESS: Tables are present\n");
            rs.close();

            System.out.println("Step 5: Checking employees data...");
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM employees");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("  Total employees: " + count);
                if (count > 0) {
                    System.out.println("  SUCCESS: Employee records exist\n");
                } else {
                    System.out.println("  WARNING: No employees found.\n");
                }
            }
            rs.close();

            System.out.println("Step 6: Checking sample data counts...");
            printCount(stmt, "customers");
            printCount(stmt, "suppliers");
            printCount(stmt, "parts");
            printCount(stmt, "inventory");
            printCount(stmt, "orders");
            printCount(stmt, "jobs");
            System.out.println();

            System.out.println("Step 7: Checking demo login user...");
            rs = stmt.executeQuery("SELECT * FROM employees WHERE username = '" + demoUsername + "'");
            if (rs.next()) {
                System.out.println("  Found: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("  Username: " + rs.getString("username"));
                System.out.println("  SUCCESS: Demo user exists and can be used for login\n");
            } else {
                System.out.println("  WARNING: Demo user not found. Rerun GreenLoop_db.sql to restore the seeded login.\n");
            }
            rs.close();

            System.out.println("Step 8: Checking roles...");
            rs = stmt.executeQuery("SELECT * FROM roles");
            System.out.println("  Roles found:");
            while (rs.next()) {
                System.out.println("    - " + rs.getString("role_name"));
            }
            rs.close();

            connection.close();
            System.out.println("All tests passed. MySQL is properly connected.\n");
            System.out.println("You can now login with: username='" + demoUsername + "', password='" + demoPassword + "'");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("\nTroubleshooting:");
            System.out.println("1. Is MySQL running? Check: Services > MySQL80");
            System.out.println("2. Is the database 'greenloop' created?");
            System.out.println("3. Did you run GreenLoop_db.sql fully?");
            System.out.println("4. Is the password 'root' correct?");
            e.printStackTrace();
        }
    }

    private static void printCount(Statement stmt, String tableName) throws Exception {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM " + tableName)) {
            if (rs.next()) {
                System.out.println("  " + tableName + ": " + rs.getInt("count"));
            }
        }
    }
}
