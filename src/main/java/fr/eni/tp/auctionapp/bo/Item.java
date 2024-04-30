package fr.eni.tp.auctionapp.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Item {
    private long itemId;
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
    private Collect collect;


    public Item(long itemId) {}


    public Item(String itemName, String description, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, int startingPrice, int sellingPrice, String saleStatus, Category category, List<Auction> auctions, User buyer, User seller, Collect collect) {
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
        this.collect = collect;
    }


    public Item(long itemId, String itemName, String description, LocalDateTime auctionStartingDate, LocalDateTime auctionEndingDate, int startingPrice, int sellingPrice, String saleStatus, Category category, List<Auction> auctions, User buyer, User seller, Collect collect) {
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
        this.collect = collect;
    }


    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
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


    public Collect getCollect() {
        return collect;
    }

    public void setCollect(Collect collect) {
        this.collect = collect;
    }
}
