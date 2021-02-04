package pl.edu.pjwstk.jaz.entity;

import javax.persistence.*;

@Entity
@Table(name = "auction_parameter")
public class AuctionParameterEntity {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionEntity auctionEntity;
    @OneToOne
    @JoinColumn(name = "parameter_id")
    private ParameterEntity parameterEntity;
    @Column(name = "value")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionEntity getAuctionEntity() {
        return auctionEntity;
    }

    public void setAuctionEntity(AuctionEntity auction) {
        this.auctionEntity = auction;
    }

    public ParameterEntity getParameterEntity() {
        return parameterEntity;
    }

    public void setParameterEntity(ParameterEntity parameterEntity) {
        this.parameterEntity = parameterEntity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AuctionParameterEntity withAuction(AuctionEntity auctionEntity) {
        this.auctionEntity = auctionEntity;
        return this;
    }

    public AuctionParameterEntity withParameter(ParameterEntity parameterEntity) {
        this.parameterEntity = parameterEntity;
        return this;
    }

    public AuctionParameterEntity withValue(String value) {
        this.value = value;
        return this;
    }
}
