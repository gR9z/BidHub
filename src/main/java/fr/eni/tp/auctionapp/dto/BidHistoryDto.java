package fr.eni.tp.auctionapp.dto;

import java.time.LocalDateTime;

public class BidHistoryDto {
    private LocalDateTime auctionDate;
    private int bidAmount;
    private int userId;
    private String username;
    private int totalCount;
    private String lastBidUser;

    public BidHistoryDto() {}

    public BidHistoryDto(LocalDateTime auctionDate, int bidAmount, int userId, String username, int totalCount, String lastBidUser) {
        this.auctionDate = auctionDate;
        this.bidAmount = bidAmount;
        this.userId = userId;
        this.username = username;
        this.totalCount = totalCount;
        this.lastBidUser = lastBidUser;
    }

    public LocalDateTime getAuctionDate() {
        return auctionDate;
    }

    public void setAuctionDate(LocalDateTime auctionDate) {
        this.auctionDate = auctionDate;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getLastBidUser() {
        return lastBidUser;
    }

    public void setLastBidUser(String lastBidUser) {
        this.lastBidUser = lastBidUser;
    }

    @Override
    public String toString() {
        return "BidHistoryDto{" +
                "auctionDate=" + auctionDate +
                ", bidAmount=" + bidAmount +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", totalCount=" + totalCount +
                ", lastBidUser='" + lastBidUser + '\'' +
                '}';
    }
}
