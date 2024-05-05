package fr.eni.tp.auctionapp.bo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class Item {
    private int itemId;

    //    @NotNull(message = "Item name cannot be null")
//    @Size(max = 30, message = "Item name must not exceed 30 characters")
    private String itemName;

    //    @NotNull(message = "Description cannot be null")
//    @Size(max = 255, message = "Description must not exceed {max} characters")
    private String description;

    //    @NotNull(message = "Auction start date cannot be null")
    private LocalDateTime auctionStartingDate;

    //    @NotNull(message = "Auction end date cannot be null")
    private LocalDateTime auctionEndingDate;

    //    @Min(value = 1, message = "The starting price must be greater than 0")
    private int startingPrice;
    private int sellingPrice;

    @Pattern(regexp = ".*\\.(jpeg|jpg|png|gif)$", message = "Invalid image file format")
    private String imageUrl;

    private String saleStatus;

    //    @NotNull(message = "Category cannot be null")
    private Category category;

    private List<Auction> auctions;
    private User buyer;
    private User seller;
    private Withdrawal withdrawal;

    public Item() {
    }

    public Item(String itemName, String description, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, int startingPrice, int sellingPrice, String imageUrl) {
        this.itemName = itemName;
        this.description = description;
        this.auctionStartingDate = auctionStartingDate;
        this.auctionEndingDate = auctionEndingDate;
        this.startingPrice = startingPrice;
        this.sellingPrice = sellingPrice;
        this.imageUrl = imageUrl;
    }

    public Item(int itemId, String itemName, String description, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, int startingPrice, int sellingPrice, String imageUrl) {
        this(itemName, description, auctionStartingDate, auctionEndingDate, startingPrice, sellingPrice, imageUrl);
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAuctionStartingDate() {
        return auctionStartingDate;
    }

    public void setAuctionStartingDate(LocalDateTime auctionStartingDate) {
        this.auctionStartingDate = auctionStartingDate;
    }

    public LocalDateTime getAuctionEndingDate() {
        return auctionEndingDate;
    }

    public void setAuctionEndingDate(LocalDateTime auctionEndingDate) {
        this.auctionEndingDate = auctionEndingDate;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Withdrawal getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Withdrawal withdrawal) {
        this.withdrawal = withdrawal;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", auctionStartingDate=" + auctionStartingDate +
                ", auctionEndingDate=" + auctionEndingDate +
                ", startingPrice=" + startingPrice +
                ", sellingPrice=" + sellingPrice +
                ", imageUrl='" + imageUrl + '\'' +
                ", saleStatus='" + saleStatus + '\'' +
                ", category=" + category +
                ", auctions=" + auctions +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", withdrawal=" + withdrawal +
                '}';
    }
}
