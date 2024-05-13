package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bo.Withdrawal;

import java.util.Optional;

public interface WithdrawalService {

    void createWithdrawal(Withdrawal Withdrawal);

    Optional<Withdrawal> findWithdrawalByItemId(int itemId);

    void updateWithdrawal(Withdrawal withdrawal);

    void removeWithdrawalByItemId(int itemId);

}
