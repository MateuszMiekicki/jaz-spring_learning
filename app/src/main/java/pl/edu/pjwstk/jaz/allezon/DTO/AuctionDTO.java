package pl.edu.pjwstk.jaz.allezon.DTO;

import java.util.List;

public class AuctionDTO {
    private Long auctionId;
    private Long authorId;
    private Long categoryId;
    private Long subcategoryId;
    private String title;
    private String description;
    private Double price;
    private List<AuctionImageDTO> images;

    public AuctionDTO(){

    }

    public AuctionDTO(Long auctionId, Long authorId, Long categoryId, Long subcategoryId, String title, String description, Double price, List<AuctionImageDTO> images) {
        this.auctionId = auctionId;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    public AuctionDTO(Long categoryId, Long subcategoryId, String title, String description, Double price, List<AuctionImageDTO> images) {
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
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

    public List<AuctionImageDTO> getImages() {
        return images;
    }

    public void setImages(List<AuctionImageDTO> images) {
        this.images = images;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }
}
