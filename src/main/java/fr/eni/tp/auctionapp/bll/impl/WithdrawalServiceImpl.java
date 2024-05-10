package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.WithdrawalService;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.dal.WithdrawalDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalDao withdrawalDao;

    public WithdrawalServiceImpl(WithdrawalDao withdrawalDao) {
        this.withdrawalDao = withdrawalDao;
    }

    @Override
    public void createWithdrawal(Withdrawal withdrawal) {
        withdrawalDao.insert(withdrawal);
    }

    @Override
    public Optional<Withdrawal> findWithdrawalByItemId(int itemId) {
        return withdrawalDao.getByItemId(itemId);
    }

    @Override
    public void updateWithdrawal(Withdrawal withdrawal) {
        withdrawalDao.update(withdrawal);
    }

    @Override
    public void removeWithdrawalByItemId(int itemId) {
        withdrawalDao.deleteByItemId(itemId);
    }
}
