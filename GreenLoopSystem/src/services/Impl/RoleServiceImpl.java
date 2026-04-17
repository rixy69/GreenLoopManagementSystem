package services.Impl;

import models.Role;
import services.DatabaseConnectionService;
import services.RoleService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final DatabaseConnectionService dbService;

    public RoleServiceImpl(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    @Override
    public boolean addRole(Role role) {
        String sql = "INSERT INTO greenloop.roles (role_name) VALUES (?)";
        try (Connection conn = dbService.getConnection()) {
            if (conn == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, role.getRoleName());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Role getRoleById(int id) {
        String sql = "SELECT role_id, role_name FROM greenloop.roles WHERE role_id = ?";
        Role role = null;
        try (Connection conn = dbService.getConnection()) {
            if (conn == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        String sql = "SELECT role_id, role_name FROM greenloop.roles";
        List<Role> roles = new ArrayList<>();
        try (Connection conn = dbService.getConnection()) {
            if (conn == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    roles.add(new Role(rs.getInt("role_id"), rs.getString("role_name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public boolean updateRole(Role role) {
        String sql = "UPDATE greenloop.roles SET role_name = ? WHERE role_id = ?";
        boolean rowUpdated = false;
        try (Connection conn = dbService.getConnection()) {
            if (conn == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, role.getRoleName());
                stmt.setInt(2, role.getRoleId());
                rowUpdated = stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteRole(int id) {
        String sql = "DELETE FROM greenloop.roles WHERE role_id = ?";
        boolean rowDeleted = false;
        try (Connection conn = dbService.getConnection()) {
            if (conn == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                rowDeleted = stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }
}
