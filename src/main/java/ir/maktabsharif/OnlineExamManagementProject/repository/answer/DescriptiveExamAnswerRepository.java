package ir.maktabsharif.OnlineExamManagementProject.repository.answer;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.DescriptiveExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptiveExamAnswerRepository extends JpaRepository<DescriptiveExamAnswer, Long> {
}

