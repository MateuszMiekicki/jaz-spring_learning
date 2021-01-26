package pl.edu.pjwstk.jaz.allezon.DTO;

public class SubcategoryDTO {
    private Long categoryId;
    private String name;

    public SubcategoryDTO(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public SubcategoryDTO() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
    
}
