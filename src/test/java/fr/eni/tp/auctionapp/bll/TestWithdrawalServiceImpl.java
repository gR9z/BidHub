package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bll.impl.WithdrawalServiceImpl;
import fr.eni.tp.auctionapp.bo.Withdrawal;
import fr.eni.tp.auctionapp.dal.WithdrawalDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestWithdrawalServiceImpl {

    @Mock
    private WithdrawalDao withdrawalDao;

    @InjectMocks
    private WithdrawalServiceImpl withdrawalService;

    private Withdrawal withdrawal;

    @BeforeEach
    public void setUp() {
        withdrawal = new Withdrawal();
    }

    @Test
    void test_createWithdrawal() {
        doNothing().when(withdrawalDao).insert(withdrawal);

        withdrawalService.createWithdrawal(withdrawal);

        verify(withdrawalDao, times(1)).insert(withdrawal);
    }

    @Test
    void test_findWithdrawalByItemId() {
        Withdrawal withdrawal = new Withdrawal();
        when(withdrawalDao.getByItemId(anyInt())).thenReturn(Optional.of(withdrawal));

        Optional<Withdrawal> result = withdrawalService.findWithdrawalByItemId(1);

        assertThat(result.isPresent()).isTrue();
        verify(withdrawalDao, times(1)).getByItemId(1);
    }

    @Test
    void test_updateWithdrawal() {
        Withdrawal withdrawal = new Withdrawal();
        doNothing().when(withdrawalDao).update(withdrawal);

        withdrawalService.updateWithdrawal(withdrawal);

        verify(withdrawalDao, times(1)).update(withdrawal);
    }

    @Test
    void test_removeWithdrawalByItemId() {
        doNothing().when(withdrawalDao).deleteByItemId(anyInt());

        withdrawalService.removeWithdrawalByItemId(1);

        verify(withdrawalDao, times(1)).deleteByItemId(1);
    }
}