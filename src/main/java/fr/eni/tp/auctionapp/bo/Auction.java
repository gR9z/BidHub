package fr.eni.tp.auctionapp.bo;

public class Auction {
    private int auctionId;
    private int userId;
    private int itemId;
    private int auctionDate;
    private int bidAmount;

    public Auction() {
    }

    public Auction(int userId, int itemId, int bidAmount) {
        this.userId = userId;
        this.itemId = itemId;
        this.bidAmount = bidAmount;
    }

    public Auction(int userId, int itemId, int auctionDate, int bidAmount) {
        this(userId, itemId, bidAmount);
        this.auctionDate = auctionDate;

    }

    public Auction(int auctionId, int userId, int itemId, int auctionDate, int bidAmount) {
        this(userId, itemId, auctionDate, bidAmount);
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
        return bidAmount;
    }

    public void setAuctionPrice(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Auction{");
        sb.append("auctionId=").append(auctionId);
        sb.append(", userId=").append(userId);
        sb.append(", itemId=").append(itemId);
        sb.append(", auctionDate=").append(auctionDate);
        sb.append(", bidAmount=").append(bidAmount);
        sb.append('}');
        return sb.toString();
    }
}
