package fr.eni.tp.auctionapp.bll;

import fr.eni.tp.auctionapp.bll.impl.UserServiceImpl;
import fr.eni.tp.auctionapp.bo.User;
import fr.eni.tp.auctionapp.dal.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestUserServiceImpl {

    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private User adminUser;

    @BeforeEach
    public void setUp() {
        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setAdmin(true);
    }

    @Test
    void test_createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(passwordEncoderMock.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        doNothing().when(userDaoMock).insert(user);

        userService.createUser(user);

        verify(userDaoMock, times(1)).insert(user);

        verify(passwordEncoderMock, times(1)).encode(eq("password"));
    }

    @Test
    void test_loadUserByUsername() {
        when(userDaoMock.selectUserByUsername(adminUser.getUsername())).thenReturn(Optional.of(adminUser));

        UserDetails loadedUser = userService.loadUserByUsername(adminUser.getUsername());
        assertThat(loadedUser).isNotNull();
        assertThat(loadedUser.getUsername()).isEqualTo(adminUser.getUsername());
        assertThat(loadedUser.getPassword()).isEqualTo(adminUser.getPassword());
    }

    @Test
    void test_loadUserByUsername_UserNotFound() {
        String nonExistingUsername = "nonexistinguser";
        when(userDaoMock.selectUserByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(nonExistingUsername))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void test_getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userDaoMock.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(users.size());

        verify(userDaoMock).findAll();
    }

    @Test
    void test_getTotalUserCount() {
        int count = 10;
        when(userDaoMock.count()).thenReturn(count);

        int result = userService.getTotalUserCount();
        assertThat(result).isEqualTo(count);

        verify(userDaoMock).count();
    }
}
