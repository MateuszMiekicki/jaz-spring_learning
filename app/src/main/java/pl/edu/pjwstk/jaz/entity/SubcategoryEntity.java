package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;

@Entity
@Table(name = "subcategory")
public class SubcategoryEntity {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;
    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubcategoryEntity withCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
        return this;
    }

    public SubcategoryEntity withName(String name) {
        this.name = name;
        return this;
    }
}
