package ir.maktabsharif.OnlineExamManagementProject.model.entity.question;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.answer.MCQExamAnswer;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Options extends BaseEntity<Long> {

    private String text;

    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultipleChoiceQuestion question;

    @OneToMany(mappedBy = "selectedOption")
    private List<MCQExamAnswer> answers;


    public static OptionsBuilder builder() {
        return new OptionsBuilder();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public MultipleChoiceQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MultipleChoiceQuestion question) {
        this.question = question;
    }

    public List<MCQExamAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<MCQExamAnswer> answers) {
        this.answers = answers;
    }

    public static class OptionsBuilder {
        private Options options;
        public OptionsBuilder() {
            options = new Options();
        }

        public OptionsBuilder question(MultipleChoiceQuestion question) {
            options.setQuestion(question);
            return this;
        }

        public OptionsBuilder text(String text) {
            options.setText(text);
            return this;
        }

        public OptionsBuilder isCorrect(Boolean isCorrect) {
            options.setCorrect(isCorrect);
            return this;
        }

        public Options build() {
            return options;
        }
    }

}
