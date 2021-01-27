package pl.edu.pjwstk.jaz.allezon.DTO;

import java.util.List;

public class AuctionDTO {
    private Long auctionId;
    private Long authorId;
    private String categoryName;
    private String subcategoryName;
    private String title;
    private String description;
    private Double price;
    private List<AuctionImageDTO> images;
    private List<AuctionParameterDTO> parameters;

    public AuctionDTO(){
    }

    public AuctionDTO(Long auctionId) {
        this.auctionId = auctionId;
    }

    public AuctionDTO(String categoryName, String subcategoryName, String title, String description, Double price, List<AuctionImageDTO> images, List<AuctionParameterDTO> parameters) {
        this.categoryName = categoryName;
        this.subcategoryName = subcategoryName;
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
        this.parameters = parameters;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
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

    public List<AuctionParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<AuctionParameterDTO> parameters) {
        this.parameters = parameters;
    }
}
