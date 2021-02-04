package pl.edu.pjwstk.jaz.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.DTO.AuctionDTO;
import pl.edu.pjwstk.jaz.DTO.AuctionImageDTO;
import pl.edu.pjwstk.jaz.DTO.AuctionParameterDTO;
import pl.edu.pjwstk.jaz.entity.*;
import pl.edu.pjwstk.jaz.repository.*;
import pl.edu.pjwstk.jaz.security.UserSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AuctionRestController {
    private final AuctionRepository auctionRepository;
    private final AuctionImageRepository auctionImageRepository;
    private final ParameterRepository parameterRepository;
    private final AuctionParameterRepository auctionParameterRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserSession userSession;
    private final UserRepository userRepository;

    public AuctionRestController(AuctionRepository auctionRepository, AuctionImageRepository auctionImageRepository, ParameterRepository parameterRepository, AuctionParameterRepository auctionParameterRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, UserSession userSession, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.auctionImageRepository = auctionImageRepository;
        this.parameterRepository = parameterRepository;
        this.auctionParameterRepository = auctionParameterRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.userSession = userSession;
        this.userRepository = userRepository;
    }

    @GetMapping("allezon/auctions")
    public ResponseEntity<String> getAuctions() {
        //ToDo: poprawic wypisanie za pomoca obiektow json
        StringBuilder output = new StringBuilder();
        for (AuctionEntity auctionEntity : auctionRepository.findAll()) {
            output.append('\n');
            output.append(auctionEntity.getId());
            output.append('\n');
            output.append("email " + auctionEntity.getAuthor().getEmail());
            output.append('\n');
            output.append(auctionEntity.getAuctionCategory().getName());
            output.append('\n');
            output.append(auctionEntity.getAuctionSubcategory().getName());
            output.append('\n');
            output.append(auctionEntity.getPrice());
            output.append('\n');
            for (AuctionImageEntity auctionImageEntity : auctionEntity.getImages()) {
                output.append(auctionImageEntity.getUrl());
                output.append('\n');
            }
            for (AuctionParameterEntity auctionParameterEntity : auctionEntity.getParameters()) {
                output.append(auctionParameterEntity.getValue());
                output.append('\n');
                output.append(auctionParameterEntity.getParameterEntity().getName());
                output.append('\n');
            }
            output.append("------------------------------------------");
            output.append('\n');
        }
        return new ResponseEntity<>(output.toString(), HttpStatus.OK);
    }

    @GetMapping("allezon/auctions/{userEmail}")
    public void getAuctions(@PathVariable("userEmail") String userEmail, HttpServletResponse response) throws IOException {
        JSONArray auctions = new JSONArray();
        UserEntity user = userRepository.findByEmail(userEmail);
        if(user==null){
            response.getWriter().write(auctions.toString());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        for (AuctionEntity auctionEntity : auctionRepository.findAllByAuthor(user)) {
            auctions.put(auctionEntity.getId());
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(auctions.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PostMapping("allezon/auctions")
    public ResponseEntity<String> addAuction(@RequestBody AuctionDTO auctionDTO) {
        if (auctionDTO.getCategoryName().isEmpty() ||
                auctionDTO.getSubcategoryName().isEmpty() ||
                auctionDTO.getTitle().isEmpty() ||
                auctionDTO.getDescription().isEmpty() ||
                auctionDTO.getPrice().equals(null)) {
            return new ResponseEntity<>("Incomplete data.", HttpStatus.NOT_ACCEPTABLE);
        }
        CategoryEntity category = categoryRepository.findByName(auctionDTO.getCategoryName());
        if (category == null) {
            return new ResponseEntity<>("The selected category does not exist.", HttpStatus.NOT_FOUND);
        }
        SubcategoryEntity subcategory = subcategoryRepository.findByCategoryEntityAndName(category, auctionDTO.getSubcategoryName());
        if (subcategory == null) {
            return new ResponseEntity<>("The selected subcategory does not exist.", HttpStatus.NOT_FOUND);
        }
        for (AuctionParameterDTO auctionParameterDTO : auctionDTO.getAuctionParameters()) {
            ParameterEntity parameter = parameterRepository.findByName(auctionParameterDTO.getName());
            if (parameter == null) {
                return new ResponseEntity<>("The selected parameter does not exist.", HttpStatus.NOT_FOUND);
            }
        }
        AuctionEntity auction = auctionRepository.save(new AuctionEntity()
                .withAuthor(userSession.getUserEntity())
                .withTitle(auctionDTO.getTitle())
                .withPrice(auctionDTO.getPrice())
                .withDescription(auctionDTO.getDescription())
                .withCategory(category)
                .withSubcategory(subcategory));

        List<AuctionImageEntity> images = new ArrayList<>();
        for (AuctionImageDTO auctionImageDTO : auctionDTO.getAuctionImages()) {
            images.add(new AuctionImageEntity()
                    .withAuction(auction)
                    .withURL(auctionImageDTO.getUrl()));
        }

        List<AuctionParameterEntity> parameters = new ArrayList<>();
        for (AuctionParameterDTO auctionParameterDTO : auctionDTO.getAuctionParameters()) {
            parameters.add(new AuctionParameterEntity()
                    .withAuction(auction)
                    .withParameter(parameterRepository.findByName(auctionParameterDTO.getName()))
                    .withValue(auctionParameterDTO.getValue()));
        }
        auctionImageRepository.saveAll(images);
        auctionParameterRepository.saveAll(parameters);
        return new ResponseEntity<>("Added auction.", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @DeleteMapping("allezon/auctions")
    public ResponseEntity<String> deleteAuction(@RequestBody AuctionDTO auctionDTO) {
        Optional<AuctionEntity> auction = auctionRepository.findById(auctionDTO.getAuctionId());
        if (auction.isPresent()) {
            if (userSession.getUserEntity().getRoleEntity().getId().longValue() == 1L ||
                    userSession.getUserEntity().getId().equals(auction.get().getAuthor().getId())) {
                auctionRepository.delete(auction.get());
            } else {
                return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Such an auction does not exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Deleted auction.", HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PutMapping("allezon/auctions")
    public ResponseEntity<String> editAuction(@RequestBody AuctionDTO auctionDTO) {
        Optional<AuctionEntity> auction = auctionRepository.findById(auctionDTO.getAuctionId());
        if (auction.isPresent()) {
            if (userSession.getUserEntity().getRoleEntity().getId().longValue() == 1L ||
                    userSession.getUserEntity().getId().equals(auction.get().getAuthor().getId())) {
                auction.get().setAuthor(auction.get().getAuthor());
                if (auctionDTO.getTitle() != null && !auctionDTO.getTitle().isEmpty()) {
                    auction.get().setTitle(auctionDTO.getTitle());
                }
                if (auctionDTO.getPrice() != null) {
                    auction.get().setPrice(auctionDTO.getPrice());
                }
                if (auctionDTO.getDescription() != null) {
                    auction.get().setDescription(auctionDTO.getDescription());
                }
                if ((auctionDTO.getCategoryName() != null && !auctionDTO.getCategoryName().isEmpty()) &&
                        (auctionDTO.getSubcategoryName() != null && !auctionDTO.getSubcategoryName().isEmpty())) {
                    CategoryEntity category = categoryRepository.findByName(auctionDTO.getCategoryName());
                    if (category == null) {
                        return new ResponseEntity<>("The selected category does not exist.", HttpStatus.NOT_FOUND);
                    }
                    auction.get().setAuctionCategory(category);
                    SubcategoryEntity subcategory = subcategoryRepository.findByCategoryEntityAndName(category, auctionDTO.getSubcategoryName());
                    if (subcategory == null) {
                        return new ResponseEntity<>("The selected subcategory does not exist.", HttpStatus.NOT_FOUND);
                    }
                    auction.get().setAuctionSubcategory(subcategory);
                } else if ((auctionDTO.getCategoryName() != null && !auctionDTO.getCategoryName().isEmpty()) &&
                        (auctionDTO.getSubcategoryName() == null)) {
                    CategoryEntity category = auction.get().getAuctionCategory();
                    auction.get().setAuctionCategory(category);
                    SubcategoryEntity subcategory = subcategoryRepository.findByCategoryEntityAndName(category, auctionDTO.getSubcategoryName());
                    if (subcategory == null) {
                        return new ResponseEntity<>("The selected subcategory does not exist.", HttpStatus.NOT_FOUND);
                    }
                    auction.get().setAuctionSubcategory(subcategory);
                } else if ((auctionDTO.getCategoryName() == null) &&
                        (auctionDTO.getSubcategoryName() != null && !auctionDTO.getSubcategoryName().isEmpty())) {
                    CategoryEntity category = auction.get().getAuctionCategory();
                    auction.get().setAuctionCategory(category);
                    SubcategoryEntity subcategory = subcategoryRepository.findByCategoryEntityAndName(category, auctionDTO.getSubcategoryName());
                    if (subcategory == null) {
                        return new ResponseEntity<>("The selected subcategory does not exist.", HttpStatus.NOT_FOUND);
                    }
                    auction.get().setAuctionSubcategory(subcategory);
                }
                if (auctionDTO.getAuctionImages() != null && !auctionDTO.getAuctionImages().isEmpty()) {
                    List<AuctionImageEntity> images = new ArrayList<>();
                    for (AuctionImageDTO auctionImageDTO : auctionDTO.getAuctionImages()) {
                        images.add(new AuctionImageEntity()
                                .withAuction(auction.get())
                                .withURL(auctionImageDTO.getUrl()));
                    }
                    auctionImageRepository.saveAll(images);
                }
                if (auctionDTO.getAuctionParameters() != null && !auctionDTO.getAuctionParameters().isEmpty()) {
                    List<AuctionParameterEntity> parameters = new ArrayList<>();
                    for (AuctionParameterDTO auctionParameterDTO : auctionDTO.getAuctionParameters()) {
                        parameters.add(new AuctionParameterEntity()
                                .withAuction(auction.get())
                                .withParameter(parameterRepository.findByName(auctionParameterDTO.getName()))
                                .withValue(auctionParameterDTO.getValue()));
                    }
                    auctionParameterRepository.saveAll(parameters);
                }
                auctionRepository.save(auction.get());
                return new ResponseEntity<>("Edited auction", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Such an auction does not exist.", HttpStatus.NOT_FOUND);
    }
}
