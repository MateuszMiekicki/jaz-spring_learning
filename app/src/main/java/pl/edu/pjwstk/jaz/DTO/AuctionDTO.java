package pl.edu.pjwstk.jaz.DTO;

import java.util.List;

public class AuctionDTO {
    private Long auctionId;
    private String authorEmail;
    private String categoryName;
    private String subcategoryName;
    private String title;
    private String description;
    private Double price;
    private List<AuctionImageDTO> auctionImages;
    private List<AuctionParameterDTO> auctionParameters;

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }
    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
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

    public List<AuctionImageDTO> getAuctionImages() {
        return auctionImages;
    }

    public void setAuctionImages(List<AuctionImageDTO> auctionImages) {
        this.auctionImages = auctionImages;
    }

    public List<AuctionParameterDTO> getAuctionParameters() {
        return auctionParameters;
    }

    public void setAuctionParameters(List<AuctionParameterDTO> auctionParameters) {
        this.auctionParameters = auctionParameters;
    }
}
