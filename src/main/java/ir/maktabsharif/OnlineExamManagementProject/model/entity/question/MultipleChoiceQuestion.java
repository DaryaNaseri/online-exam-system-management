package ir.maktabsharif.OnlineExamManagementProject.model.entity.question;


import ir.maktabsharif.OnlineExamManagementProject.model.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MULTI")
public class MultipleChoiceQuestion extends Question {

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Options> options = new ArrayList<>();

    public static MultipleChoiceQuestionBuilder builder() {
        return new MultipleChoiceQuestionBuilder();
    }

    public @NotNull List<Options> getOptions() {
        return options;
    }

    public void setOptions(@NotNull List<Options> options) {
        this.options = options;
    }

    public static class MultipleChoiceQuestionBuilder {
        private MultipleChoiceQuestion question;

        public MultipleChoiceQuestionBuilder() {
            question = new MultipleChoiceQuestion();
        }

        public MultipleChoiceQuestionBuilder options(List<Options> options) {
            question.setOptions(options);
            return this;
        }

        public MultipleChoiceQuestionBuilder title(String title) {
            question.setTitle(title);
            return this;
        }

        public MultipleChoiceQuestionBuilder teacher(Teacher teacher) {
            question.setTeacher(teacher);
            return this;
        }

        public MultipleChoiceQuestionBuilder content(String content) {
            question.setContent(content);
            return this;
        }

        public MultipleChoiceQuestion build() {
            return question;
        }
    }
}
