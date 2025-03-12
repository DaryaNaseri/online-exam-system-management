package ir.maktabsharif.OnlineExamManagementProject.model.entity.question;


import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DESCRIPTIVE")
public class DescriptiveQuestion extends Question {

    public static DescriptiveQuestionBuilder builder() {
        return new DescriptiveQuestionBuilder();
    }

    public static class DescriptiveQuestionBuilder {
        private DescriptiveQuestion question;
        public DescriptiveQuestionBuilder() {
            question = new DescriptiveQuestion();
        }
        public DescriptiveQuestionBuilder title(String title) {
            question.setTitle(title);
            return this;
        }

        public DescriptiveQuestionBuilder content(String content) {
            question.setContent(content);
            return this;
        }

        public DescriptiveQuestionBuilder teacher(Teacher teacher) {
            question.setTeacher(teacher);
            return this;
        }
        public DescriptiveQuestion build() {
            return question;
        }
    }

}
