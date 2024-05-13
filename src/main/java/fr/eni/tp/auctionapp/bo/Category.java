package fr.eni.tp.auctionapp.bo;

import java.util.List;

public class Category {
    private int categoryId;
    private String label;

    private List<Item> items;
    private int numberOfItems;

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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", label='" + label + '\'' +
                ", items=" + items +
                ", numberOfItems=" + numberOfItems +
                '}';
    }
}
