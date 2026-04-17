package services.Impl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import services.DatabaseConnectionService;

public class MySQLConnectionService implements DatabaseConnectionService {

    private String username = readStringConfig("GREENLOOP_DB_USER", "greenloop.db.user", "root");
    private String password = readStringConfig("GREENLOOP_DB_PASSWORD", "greenloop.db.password", "root");
    private String dbName = readStringConfig("GREENLOOP_DB_NAME", "greenloop.db.name", "greenloop");
    private String host = readStringConfig("GREENLOOP_DB_HOST", "greenloop.db.host", "localhost");
    private int port = readIntConfig("GREENLOOP_DB_PORT", "greenloop.db.port", 3306);

    public MySQLConnectionService(){}

    public MySQLConnectionService(String username, String password, String dbName, String host, int port) {
        this.username = username;
        this.password = password;
        this.dbName = dbName;
        this.host = host;
        this.port = port;
    }

    @Override
    public Connection getConnection() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            ensureDriverLoaded();
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed. Please check MySQL is running, the 'greenloop' database exists, and credentials are correct. Error: " + e.getMessage(), e);
        }
    }

    private void ensureDriverLoaded() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            File jarFile = new File("lib/mysql-connector-j-8.2.0.jar");
            if (!jarFile.exists()) {
                throw new RuntimeException("MySQL JDBC driver not found, and the connector jar is missing at lib/mysql-connector-j-8.2.0.jar. Please add it to the classpath.", e);
            }

            try {
                URL jarUrl = jarFile.toURI().toURL();
                try (URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, ClassLoader.getSystemClassLoader())) {
                    Class<?> driverClass = Class.forName("com.mysql.cj.jdbc.Driver", true, loader);
                    Object driver = driverClass.getDeclaredConstructor().newInstance();
                    DriverManager.registerDriver(new DriverShim((java.sql.Driver) driver));
                }
            } catch (Exception ex) {
                throw new RuntimeException("MySQL JDBC driver not found and failed to load the connector jar dynamically. Please ensure mysql-connector-j is on the classpath.", ex);
            }
        }
    }

    private static class DriverShim implements java.sql.Driver {
        private final java.sql.Driver driver;

        DriverShim(java.sql.Driver driver) {
            this.driver = driver;
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public Connection connect(String url, java.util.Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private static String readStringConfig(String envKey, String propertyKey, String defaultValue) {
        String propertyValue = System.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue.trim();
        }

        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        return defaultValue;
    }

    private static int readIntConfig(String envKey, String propertyKey, int defaultValue) {
        String value = readStringConfig(envKey, propertyKey, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
