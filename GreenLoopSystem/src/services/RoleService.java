package services;

import models.Role;
import java.util.List;

public interface RoleService {
    boolean addRole(Role role);
    Role getRoleById(int id);
    List<Role> getAllRoles();
    boolean updateRole(Role role);
    boolean deleteRole(int id);
}
