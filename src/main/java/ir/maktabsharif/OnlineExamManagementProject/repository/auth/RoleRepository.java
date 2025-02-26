package ir.maktabsharif.OnlineExamManagementProject.repository.auth;

import ir.maktabsharif.OnlineExamManagementProject.model.permision.Authority;
import ir.maktabsharif.OnlineExamManagementProject.model.permision.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
