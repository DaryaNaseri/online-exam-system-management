package ir.maktabsharif.OnlineExamManagementProject.repository.answer;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.MCQExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MCQExamAnswerRepository extends JpaRepository<MCQExamAnswer, Long> {
}
