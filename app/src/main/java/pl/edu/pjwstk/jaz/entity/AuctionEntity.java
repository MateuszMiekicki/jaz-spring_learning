package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "auction")
public class AuctionEntity {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity auctionCategory;
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubcategoryEntity auctionSubcategory;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
    @OneToMany(mappedBy = "auctionEntity", cascade = CascadeType.ALL)
    private List<AuctionImageEntity> images;
    @OneToMany(mappedBy = "auctionEntity", cascade = CascadeType.ALL)
    private List<AuctionParameterEntity> parameters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public CategoryEntity getAuctionCategory() {
        return auctionCategory;
    }

    public void setAuctionCategory(CategoryEntity auctionCategory) {
        this.auctionCategory = auctionCategory;
    }

    public SubcategoryEntity getAuctionSubcategory() {
        return auctionSubcategory;
    }

    public void setAuctionSubcategory(SubcategoryEntity auctionSubcategory) {
        this.auctionSubcategory = auctionSubcategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<AuctionImageEntity> getImages() {
        return images;
    }

    public void setImages(List<AuctionImageEntity> images) {
        this.images = images;
    }

    public List<AuctionParameterEntity> getParameters() {
        return parameters;
    }

    public void setParameters(List<AuctionParameterEntity> parameters) {
        this.parameters = parameters;
    }

    public AuctionEntity withAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public AuctionEntity withCategory(CategoryEntity auctionCategory) {
        this.auctionCategory = auctionCategory;
        return this;
    }

    public AuctionEntity withSubcategory(SubcategoryEntity auctionSubcategory) {
        this.auctionSubcategory = auctionSubcategory;
        return this;
    }

    public AuctionEntity withTitle(String title) {
        this.title = title;
        return this;
    }

    public AuctionEntity withDescription(String description) {
        this.description = description;
        return this;
    }

    public AuctionEntity withPrice(Double price) {
        this.price = price;
        return this;
    }

    public AuctionEntity withImages(List<AuctionImageEntity> images) {
        this.images = images;
        return this;
    }

    public AuctionEntity withParameters(List<AuctionParameterEntity> parameters) {
        this.parameters = parameters;
        return this;
    }
}
