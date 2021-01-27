package pl.edu.pjwstk.jaz.allezon.DTO;

public class AuctionParameterDTO {
    private Long auctionId;
    private String name;
    private String value;

    public AuctionParameterDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
