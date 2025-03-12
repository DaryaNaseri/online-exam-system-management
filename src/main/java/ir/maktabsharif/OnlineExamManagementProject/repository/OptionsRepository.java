package ir.maktabsharif.OnlineExamManagementProject.repository;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsRepository extends JpaRepository<Options, Long> {
}
