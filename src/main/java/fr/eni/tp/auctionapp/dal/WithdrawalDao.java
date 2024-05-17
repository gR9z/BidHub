package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Withdrawal;

import java.util.Optional;

public interface WithdrawalDao {
    void insert(Withdrawal withdrawal);
    Optional<Withdrawal> getByItemId(int itemId);
    void update(Withdrawal withdrawal);
    void deleteByItemId(int itemId);
}
