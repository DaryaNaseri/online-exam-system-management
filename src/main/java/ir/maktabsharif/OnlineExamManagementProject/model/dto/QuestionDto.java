package ir.maktabsharif.OnlineExamManagementProject.model.dto;

import ir.maktabsharif.OnlineExamManagementProject.model.entity.question.Options;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface QuestionDto {

    record CreateRequest(
            @NotBlank(message = "title is required, fill it.")
            @Size(min = 1, max = 50, message = "title must be short that 50 characters.")
            String title,
            @NotBlank(message = "content of question must not be empty.")
            String content,
            @NotBlank(message = "please choose what question you want to create(multi/descriptive)")
            String qType,
            List<OptionDto.CreateRequest> optionsDto
    ) {
        public boolean isMultipleChoice() {
            return "multi".equalsIgnoreCase(qType);
        }

        public boolean isDescriptive() {
            return "descriptive".equalsIgnoreCase(qType);
        }


    }

    class ResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long teacherId;
    private String qType;

        public ResponseDto(Long id, String title, String content, Long teacherId, String qType) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.teacherId = teacherId;
            this.qType = qType;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(Long teacherId) {
            this.teacherId = teacherId;
        }

        public String getqType() {
            return qType;
        }

        public void setqType(String qType) {
            this.qType = qType;
        }
    }

    class DescriptiveQuestionResponse extends ResponseDto {
            private Long id;
            private String title;
            private String content;
            private Long teacherId;

        public DescriptiveQuestionResponse(Long id, String title, String content, Long teacherId) {
            super(id, title, content, teacherId, "descriptive");
            this.id = id;
            this.title = title;
            this.content = content;
            this.teacherId = teacherId;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public Long getTeacherId() {
            return teacherId;
        }

        @Override
        public void setTeacherId(Long teacherId) {
            this.teacherId = teacherId;
        }
    }

    class MultiChoiceQuestionResponse extends ResponseDto {
        private Long id;
        private String title;
        private String content;
        private Long teacherId;
        private List<OptionDto.Response> optionsDto;

        public MultiChoiceQuestionResponse(Long id, String title, String content, Long teacherId, List<OptionDto.Response> optionsDto) {
            super(id,title, content, teacherId, "multi");
            this.id = id;
            this.title = title;
            this.content = content;
            this.teacherId = teacherId;
            this.optionsDto = optionsDto;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public Long getTeacherId() {
            return teacherId;
        }

        @Override
        public void setTeacherId(Long teacherId) {
            this.teacherId = teacherId;
        }

        public List<OptionDto.Response> getOptionsDto() {
            return optionsDto;
        }

        public void setOptionsDto(List<OptionDto.Response> optionsDto) {
            this.optionsDto = optionsDto;
        }
    }
}
