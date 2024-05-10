package fr.eni.tp.auctionapp;

import com.github.javafaker.Faker;
import fr.eni.tp.auctionapp.bll.AuctionService;
import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bll.UserService;
import fr.eni.tp.auctionapp.bo.*;
import fr.eni.tp.auctionapp.dal.AuctionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TestDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionDao auctionDao;

    private Faker faker = new Faker();

    public void clearDatabase() {

        String[] tables = new String[]{"auctions", "withdrawals", "items", "categories", "users"};

        for (String table : tables) {
            jdbcTemplate.execute("DELETE FROM " + table);
        }
    }

    public User createRandomUser() {
        User user = new User();
        user.setUsername(faker.name().username());
        user.setLastName(faker.name().lastName());
        user.setFirstName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        user.setPhone(faker.phoneNumber().phoneNumber());
        user.setStreet(faker.address().streetAddress());
        user.setZipCode(faker.address().zipCode());
        user.setCity(faker.address().city());
        user.setPassword("password");
        user.setCredit(faker.number().numberBetween(0, 5001));
        user.setAdmin(false);

        return user;
    }

    public User createRandomAdmin() {
        User user = createRandomUser();
        user.setAdmin(true);

        return user;
    }

    public User insertUserInDatabase(User user) {
        userService.createUser(user);
        return user;
    }

    public Category createRandomCategory() {
        Category category = new Category();
        category.setLabel(faker.commerce().department());

        return category;
    }

    public Category insertCategoryInDatabase(Category category) {
        categoryService.createCategory(category);
        return category;
    }

    public Item createRandomItem(User seller, Category category) {
        Item item = new Item();

        item.setItemName(faker.commerce().productName());
        item.setDescription(faker.lorem().sentence());

        java.util.Date date = faker.date().past(15, TimeUnit.DAYS);
        LocalDateTime startDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        item.setAuctionStartingDate(startDateTime);
        item.setAuctionEndingDate(startDateTime.plusDays(7));

        int startingPrice = faker.number().numberBetween(100, 500);
        item.setStartingPrice(startingPrice);
        item.setSellingPrice(startingPrice + faker.number().numberBetween(1, 2));
        item.setImageUrl(faker.internet().url());

        item.setSeller(seller);
        item.setCategory(category);

        return item;
    }

    public Item insertItemInDatabase(Item item) {
        itemService.createItem(item);
        return item;
    }

    public Auction createAuction(User user, Item item, int currentHighestBid) {
        Optional<Auction> optionalLastAuction = auctionDao.findTopByItemIdOrderByAuctionDateDesc(item.getItemId());

        LocalDateTime auctionDate;
        if (optionalLastAuction.isPresent()) {
            Auction lastAuction = optionalLastAuction.get();
            int randomMinutes = faker.number().numberBetween(1, 60);
            auctionDate = lastAuction.getAuctionDate().plusMinutes(randomMinutes);
        } else {
            LocalDateTime auctionStartDate = item.getAuctionStartingDate();
            int randomHours = faker.number().numberBetween(1, 4);
            auctionDate = auctionStartDate.plusHours(randomHours);
        }

        int newBidAmount = currentHighestBid + faker.number().numberBetween(50, 150);

        Auction auction = new Auction();
        auction.setUserId(user.getId());
        auction.setItemId(item.getItemId());
        auction.setAuctionDate(auctionDate);
        auction.setBidAmount(newBidAmount);

        return auction;
    }



    public Auction insertAuctionInDatabase(Auction auction) {
        auctionService.createAuction(auction);
        return auction;
    }

    public Withdrawal createRandomWithdrawal(Item item) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setItem(item);
        withdrawal.setItemId(item.getItemId());
        withdrawal.setCity(faker.address().city());
        withdrawal.setStreet(faker.address().streetAddress());
        withdrawal.setZipCode(faker.address().zipCode());
        item.setWithdrawal(withdrawal);

        return withdrawal;
    }
}