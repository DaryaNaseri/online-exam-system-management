package ir.maktabsharif.OnlineExamManagementProject.model.permision;

import ir.maktabsharif.OnlineExamManagementProject.model.base.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities =  new HashSet<>();

    public static RoleBuilder builder(){
        return new RoleBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public static class RoleBuilder {
        private Role role;

        public RoleBuilder() {
            this.role = new Role();
        }

        public RoleBuilder setName(String name) {
            role.setName(name);
            return this;
        }

        public RoleBuilder setAuthorities(Set<Authority> authorities) {
            role.setAuthorities(authorities);
            return this;
        }

        public Role build() {
            return role;
        }



    }

}
