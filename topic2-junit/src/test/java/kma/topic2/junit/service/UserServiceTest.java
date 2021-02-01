package kma.topic2.junit.service;

import kma.topic2.junit.model.NewUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class UserServiceTest {
    private static final String FULL_NAME = "Illya Maltsev";
    private static final String LOGIN = "imaltest";
    private static final String VALID_PASSWORD = "12345";

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUserAndFindItByLogin() {
        NewUser user = NewUser.builder()
                .fullName(FULL_NAME)
                .login(LOGIN)
                .password(VALID_PASSWORD)
                .build();

        userService.createNewUser(user);
        assertThat(userService.getUserByLogin(LOGIN)).isEqualTo(user);
    }
}
