package ir.maktabsharif.OnlineExamManagementProject.model.permision;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    public static AuthorityBuilder builder() {
        return new AuthorityBuilder();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class AuthorityBuilder{
        private Authority authority ;

        public AuthorityBuilder(){
            this.authority = new Authority();
        }

        public AuthorityBuilder name(String name){
            this.authority.setName(name);
            return this;
        }

        public Authority build(){
            return this.authority;
        }

    }
}
