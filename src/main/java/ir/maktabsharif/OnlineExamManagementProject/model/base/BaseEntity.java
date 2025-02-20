package ir.maktabsharif.OnlineExamManagementProject.model.base;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity<ID extends Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public BaseEntity(ID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
