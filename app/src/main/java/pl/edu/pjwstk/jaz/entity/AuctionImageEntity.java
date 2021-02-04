package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;

@Entity
@Table(name = "auction_image")
public class AuctionImageEntity {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionEntity auctionEntity;
    @Column(name = "url")
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionEntity getAuctionEntity() {
        return auctionEntity;
    }

    public void setAuctionEntity(AuctionEntity auctionEntity) {
        this.auctionEntity = auctionEntity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AuctionImageEntity withURL(String url) {
        this.url = url;
        return this;
    }

    public AuctionImageEntity withAuction(AuctionEntity auctionEntity) {
        this.auctionEntity = auctionEntity;
        return this;
    }
}
