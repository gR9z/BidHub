package fr.eni.tp.auctionapp.dal;

import fr.eni.tp.auctionapp.bo.Withdrawal;

public interface WithdrawalDao {
    void insert(Withdrawal withdrawal);
    Withdrawal getWithdrawalById(int itemId);
    void update(Withdrawal withdrawal);
}
