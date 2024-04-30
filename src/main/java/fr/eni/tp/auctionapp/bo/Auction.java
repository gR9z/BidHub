package fr.eni.tp.auctionapp.bo;

public class Auction {
    private int userId;
    private int itemId;
    private int auctionDate;
    private int auctionPrice;

    public Auction() {
    }

    public Auction(int userId, int itemId, int auctionDate, int auctionPrice) {
        this.userId = userId;
        this.itemId = itemId;
        this.auctionDate = auctionDate;
        this.auctionPrice = auctionPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAuctionDate() {
        return auctionDate;
    }

    public void setAuctionDate(int auctionDate) {
        this.auctionDate = auctionDate;
    }

    public int getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(int auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    @Override
    public String toString() {
        return "Auctions{" +
                "userId=" + userId +
                ", itemId=" + itemId +
                ", auctionDate=" + auctionDate +
                ", auctionPrice=" + auctionPrice +
                '}';
    }

}
