package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;

@Entity
@Table(name = "\"user\"")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public void setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserEntity withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntity withRole(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
        return this;
    }
}
