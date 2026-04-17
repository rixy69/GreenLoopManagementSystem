package services;

import java.sql.Connection;

public interface DatabaseConnectionService {
    Connection getConnection();
}
