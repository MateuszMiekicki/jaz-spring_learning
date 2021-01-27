package pl.edu.pjwstk.jaz.allezon.DTO;

public class AuctionImageDTO {
    private Long auctionId;
    private String url;

    public AuctionImageDTO() {

    }

    public AuctionImageDTO(String url) {
        this.url = url;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
