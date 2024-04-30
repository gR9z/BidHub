package fr.eni.tp.auctionapp.bo;

public class Withdrawals{
    private int itemId;
    private String street;
    private String zipCode;
    private String city;

    public Withdrawals() {
    }

    public Withdrawals(int itemId, String street, String zipCode, String city) {
        this.itemId = itemId;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public String toString() {
        return "Withdrawals{" +
                "itemId=" + itemId +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

}
