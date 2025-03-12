package ir.maktabsharif.OnlineExamManagementProject.service.factory;

import ir.maktabsharif.OnlineExamManagementProject.exception.InvalidInputException;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.DescriptiveQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Question;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class QuestionFactory {

    private final Map<String, Supplier<Question>> questionTypes =
            new HashMap<>();

    public QuestionFactory() {
//        questionTypes.put("DESCRIPTIVE", new Supplier<Question>() {
//            @Override
//            public Question get() {
//                return new DescriptiveQuestion();
//            }
//        });
//        questionTypes.put("DESCRIPTIVE", () -> new DescriptiveQuestion());
        questionTypes.put("DESCRIPTIVE", DescriptiveQuestion::new);
        questionTypes.put("MULTI", MultipleChoiceQuestion::new);
    }

    public Question createQuestion(String type) {
        Supplier<Question> supplier = questionTypes.get(type.toUpperCase());
        if (supplier != null) {
            return supplier.get();
        }
        throw new InvalidInputException("Invalid question type: " + type);
    }
}

