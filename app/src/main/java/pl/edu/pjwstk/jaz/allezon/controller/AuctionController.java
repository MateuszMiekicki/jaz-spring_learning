package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.DTO.AuctionDTO;
import pl.edu.pjwstk.jaz.allezon.DTO.AuctionImageDTO;
import pl.edu.pjwstk.jaz.allezon.DTO.AuctionParameterDTO;
import pl.edu.pjwstk.jaz.allezon.entity.*;
import pl.edu.pjwstk.jaz.allezon.repository.*;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@RestController
public class AuctionController {
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserSession userSession;
    private final AuctionImageRepository auctionImageRepository;
    private final ParameterRepository parameterRepository;
    private final AuctionParameterRepository auctionParameterRepository;
    private final EntityManager entityManager;

    public AuctionController(AuctionRepository auctionRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, UserSession userSession, AuctionImageRepository auctionImageRepository, ParameterRepository parameterRepository, AuctionParameterRepository auctionParameterRepository, EntityManager entityManager) {
        this.auctionRepository = auctionRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.userSession = userSession;
        this.auctionImageRepository = auctionImageRepository;
        this.parameterRepository = parameterRepository;
        this.auctionParameterRepository = auctionParameterRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("allezon/auctions")
    public ResponseEntity<List<AuctionEntity>> getAuctions() {
        return new ResponseEntity(auctionRepository.getAuctions(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PostMapping("allezon/auctions")
    public ResponseEntity<String> addAuction(@RequestBody AuctionDTO auctionDTO) {
        CategoryEntity categoryEntity = categoryRepository.findByName(auctionDTO.getCategoryName());
        if (categoryEntity == null) {
            return new ResponseEntity("The selected category does not exist.", HttpStatus.NOT_FOUND);
        }

        SubcategoryEntity subcategoryEntity = subcategoryRepository.findByIdCategoryAndNameSubcategory(categoryEntity.getId(),
                auctionDTO.getSubcategoryName());
        if (subcategoryEntity == null) {
            return new ResponseEntity("The selected subcategory does not exist.", HttpStatus.NOT_FOUND);
        }

        AuctionEntity auctionEntity = new AuctionEntity();
        auctionEntity.setAuthorId(userSession.getUserId());
        auctionEntity.setCategoryId(categoryEntity.getId());
        auctionEntity.setSubcategoryId(subcategoryEntity.getId());
        auctionEntity.setTitle(auctionDTO.getTitle());
        auctionEntity.setDescription(auctionDTO.getDescription());
        auctionEntity.setPrice(auctionDTO.getPrice());
        auctionRepository.addAuction(auctionEntity);

        Long auctionId = auctionRepository.getLastId();
        for (AuctionImageDTO image : auctionDTO.getImages()) {
            AuctionImageEntity auctionImageEntity = new AuctionImageEntity();
            auctionImageEntity.setAuctionId(auctionId);
            auctionImageEntity.setUrl(image.getURL());
            auctionImageRepository.addImage(auctionImageEntity);
        }
        for (AuctionParameterDTO parameter : auctionDTO.getParameters()) {
            ParameterEntity parameterEntity = parameterRepository.getByName(parameter.getName());
            if (parameterEntity == null) {
                return new ResponseEntity("The selected parameter does not exist.", HttpStatus.NOT_FOUND);
            }
            AuctionParameterEntity auctionParameterEntity = new AuctionParameterEntity();
            auctionParameterEntity.setAuctionId(auctionId);
            auctionParameterEntity.setParameterId(parameterEntity.getId());
            auctionParameterEntity.setValue(parameter.getValue());
            auctionParameterRepository.save(auctionParameterEntity);
        }
        return new ResponseEntity("Added auction", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PutMapping("allezon/auctions")
    public ResponseEntity<AuctionEntity> updateAuction(@RequestBody AuctionDTO auctionDTO) {
        AuctionEntity auctionEntity = auctionRepository.findByAuthorIdAndAuctionId(userSession.getUserId(), auctionDTO.getAuctionId());
        if (auctionEntity == null) {
            return new ResponseEntity("The selected auction does not exist.", HttpStatus.NOT_FOUND);
        }
        auctionEntity.setPrice(auctionDTO.getPrice());
        auctionEntity.setDescription(auctionDTO.getDescription());
        CategoryEntity categoryEntity = categoryRepository.findByName(auctionDTO.getCategoryName());
        if (categoryEntity == null) {
            return new ResponseEntity("The selected category does not exist.", HttpStatus.NOT_FOUND);
        }
        auctionEntity.setCategoryId(categoryEntity.getId());
        SubcategoryEntity subcategoryEntity = subcategoryRepository.findByIdCategoryAndNameSubcategory(categoryEntity.getId(),
                auctionDTO.getSubcategoryName());
        if (subcategoryEntity == null) {
            return new ResponseEntity("The selected subcategory does not exist.", HttpStatus.NOT_FOUND);
        }
        auctionEntity.setSubcategoryId(subcategoryEntity.getId());
        auctionRepository.updateAuction(auctionEntity);

        for (AuctionParameterEntity parameter : auctionParameterRepository.findByAuctionId(auctionEntity.getId())) {
            auctionParameterRepository.delete(parameter);
        }
        for (AuctionParameterDTO parameter : auctionDTO.getParameters()) {
            ParameterEntity parameterEntity = parameterRepository.getByName(parameter.getName());
            if (parameterEntity == null) {
                return new ResponseEntity("The selected parameter does not exist.", HttpStatus.NOT_FOUND);
            }
            AuctionParameterEntity auctionParameterEntity = new AuctionParameterEntity();
            auctionParameterEntity.setAuctionId(auctionEntity.getId());
            auctionParameterEntity.setParameterId(parameterEntity.getId());
            auctionParameterEntity.setValue(parameter.getValue());
            auctionParameterRepository.save(auctionParameterEntity);
        }
        for (AuctionImageEntity image : auctionImageRepository.getImages(auctionEntity.getId())) {
            auctionImageRepository.delete(image);
        }
        for (AuctionImageDTO image : auctionDTO.getImages()) {
            AuctionImageEntity auctionImageEntity = new AuctionImageEntity();
            auctionImageEntity.setAuctionId(auctionEntity.getId());
            auctionImageEntity.setUrl(image.getURL());
            auctionImageRepository.addImage(auctionImageEntity);
        }
        return new ResponseEntity("Updated auction", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @DeleteMapping("allezon/auctions")
    public ResponseEntity<String> deleteAuction(@RequestBody AuctionDTO auctionDTO) {
        AuctionEntity auctionEntity = auctionRepository.findByAuthorIdAndAuctionId(userSession.getUserId(), auctionDTO.getAuctionId());
        if (auctionEntity == null) {
            return new ResponseEntity("The selected auction does not exist.", HttpStatus.NOT_FOUND);
        }
        for (AuctionImageEntity image : auctionImageRepository.getImages(auctionEntity.getId())) {
            auctionImageRepository.delete(image);
        }
        for (AuctionParameterEntity parameter : auctionParameterRepository.findByAuctionId(auctionEntity.getId())) {
            auctionParameterRepository.delete(parameter);
        }
        auctionRepository.deleteAuction(auctionEntity);
        return new ResponseEntity("Deleted auction", HttpStatus.NO_CONTENT);
    }
}
