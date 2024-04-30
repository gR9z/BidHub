package fr.eni.tp.auctionapp.bo;

public class Category {
    private int categoryId;
    private String label;

    public Category() {
    }

    public Category(int categoryId, String label) {
        this.categoryId = categoryId;
        this.label = label;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categoryId=" + categoryId +
                ", label='" + label + '\'' +
                '}';
    }

}
