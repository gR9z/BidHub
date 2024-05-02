package fr.eni.tp.auctionapp.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Item {
    private int itemId;
    private String itemName;
    private String description;
    private LocalDateTime auctionStartingDate;
    private LocalDateTime auctionEndingDate;
    private int startingPrice;
    private int sellingPrice;
    private String saleStatus;
    private Category category;
    private List<Auction> auctions;
    private User buyer;
    private User seller;
    private Withdrawal withdrawal;


    public Item() {}


    public Item(String itemName, String description, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, int startingPrice, int sellingPrice, String saleStatus, Category category, List<Auction> auctions, User buyer, User seller, Withdrawal withdrawal) {
        this.itemName = itemName;
        this.description = description;
        this.auctionStartingDate = auctionStartingDate;
        this.auctionEndingDate = auctionEndingDate;
        this.startingPrice = startingPrice;
        this.sellingPrice = sellingPrice;
        this.saleStatus = saleStatus;
        this.category = category;
        this.auctions = auctions;
        this.buyer = buyer;
        this.seller = seller;
        this.withdrawal = withdrawal;
    }


    public Item(int itemId, String itemName, String description, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, int startingPrice, int sellingPrice, String saleStatus, Category category, List<Auction> auctions, User buyer, User seller, Withdrawal withdrawal) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.auctionStartingDate = auctionStartingDate;
        this.auctionEndingDate = auctionEndingDate;
        this.startingPrice = startingPrice;
        this.sellingPrice = sellingPrice;
        this.saleStatus = saleStatus;
        this.category = category;
        this.auctions = auctions;
        this.buyer = buyer;
        this.seller = seller;
        this.withdrawal = withdrawal;
    }

    public Item(String itemName, String description, Category category, int startingPrice, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, User seller) {
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.startingPrice = startingPrice;
        this.auctionStartingDate = auctionStartingDate;
        this.auctionEndingDate = auctionEndingDate;
        this.seller = seller;
    }

    public Item(int itemId, String itemName, String description, Category category, int sellingPrice,  int startingPrice, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, User seller) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.sellingPrice = sellingPrice;
        this.startingPrice = startingPrice;
        this.auctionStartingDate = auctionStartingDate;
        this.auctionEndingDate = auctionEndingDate;
        this.seller = seller;
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
                ", saleStatus='" + saleStatus + '\'' +
                ", category=" + category +
                ", auctions=" + auctions +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", withdrawal=" + withdrawal +
                '}';
    }
}
