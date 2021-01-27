package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.DTO.AuctionDTO;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionImageRepository;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

import java.util.List;

@RestController
public class AuctionController {
    private final AuctionRepository auctionRepository;
    private final UserSession userSession;
    private final AuctionImageRepository auctionImageRepository;

    public AuctionController(AuctionRepository auctionRepository, UserSession userSession, AuctionImageRepository auctionImageRepository) {
        this.auctionRepository = auctionRepository;
        this.userSession = userSession;
        this.auctionImageRepository = auctionImageRepository;
    }

    @GetMapping("allezon/auctions")
    public ResponseEntity<List<AuctionEntity>> getAuctions() {
        return new ResponseEntity(auctionRepository.getAuctions(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PostMapping("allezon/auctions")
    public ResponseEntity<String> addAuction(@RequestBody AuctionDTO auctionDTO) {
        auctionDTO.setAuthorId(userSession.getUserId());
        var auction = auctionRepository.addAuction(auctionDTO);
        for(var image : auctionDTO.getImages()){
            image.setAuctionId(auction.getId());
            auctionImageRepository.addImage(image);
        }
        return new ResponseEntity("Added auction", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PutMapping("allezon/auctions")
    public ResponseEntity<AuctionEntity> updateAuction(@RequestBody AuctionDTO auctionDTO) {
        auctionDTO.setAuthorId(userSession.getUserId());
        auctionRepository.updateAuction(auctionDTO);
        return new ResponseEntity("Updated auction", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @DeleteMapping("allezon/auctions")
    public ResponseEntity<String> deleteAuction() {
        //auctionRepository.delete Auction();
        return new ResponseEntity("Updated auction", HttpStatus.OK);
    }
}
