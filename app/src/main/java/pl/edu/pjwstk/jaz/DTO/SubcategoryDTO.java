package pl.edu.pjwstk.jaz.DTO;

public class SubcategoryDTO {
    private String categoryName;
    private String subcategoryName;
    private String newSubcategoryName;

    public String getNewSubcategoryName() {
        return newSubcategoryName;
    }

    public void setNewSubcategoryName(String newSubcategoryName) {
        this.newSubcategoryName = newSubcategoryName;
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
}
