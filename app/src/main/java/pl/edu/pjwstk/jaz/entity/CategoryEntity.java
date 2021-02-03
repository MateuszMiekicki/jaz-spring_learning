package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "categoryEntity")
    private List<SubcategoryEntity> subcategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubcategoryEntity> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryEntity> subcategories) {
        this.subcategories = subcategories;
    }

    public CategoryEntity withName(String name) {
        this.name = name;
        return this;
    }
}
