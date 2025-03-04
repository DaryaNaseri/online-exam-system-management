package ir.maktabsharif.OnlineExamManagementProject.repository.auth;

import ir.maktabsharif.OnlineExamManagementProject.model.permision.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(String name);

}
