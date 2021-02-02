package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;

@Entity
@Table(name = "\"role\"")
public class RoleEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "role")
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public RoleEntity withId(Long id){
        this.id = id;
        return this;
    }
}
