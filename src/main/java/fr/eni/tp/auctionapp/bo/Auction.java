package fr.eni.tp.auctionapp.bo;

public class Auction {
    private int auctionId;
    private int userId;
    private int itemId;
    private int auctionDate;
    private int auctionPrice;

    public Auction() {
    }

    public Auction(int userId, int itemId, int auctionPrice) {
        this.userId = userId;
        this.itemId = itemId;
        this.auctionPrice = auctionPrice;
    }

    public Auction(int userId, int itemId, int auctionDate, int auctionPrice) {
        this(userId, itemId, auctionPrice);
        this.auctionDate = auctionDate;

    }

    public Auction(int auctionId, int userId, int itemId, int auctionDate, int auctionPrice) {
        this(userId, itemId, auctionDate, auctionPrice);
        this.auctionId = auctionId;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
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
        final StringBuilder sb = new StringBuilder("Auction{");
        sb.append("auctionId=").append(auctionId);
        sb.append(", userId=").append(userId);
        sb.append(", itemId=").append(itemId);
        sb.append(", auctionDate=").append(auctionDate);
        sb.append(", auctionPrice=").append(auctionPrice);
        sb.append('}');
        return sb.toString();
    }
}
