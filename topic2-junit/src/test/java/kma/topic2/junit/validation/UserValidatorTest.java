package kma.topic2.junit.validation;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {
    private static final String FULL_NAME = "Illya Maltsev";
    private static final String LOGIN = "imaltest";
    private static final String VALID_PASSWORD = "12345";

    private static final String EMPTY_PASSWORD = "";
    private static final String SHORT_PASSWORD = "12";
    private static final String LONG_PASSWORD = "123456789";
    private static final String NOT_MATCHING_REGEX_PASSWORD = "\\\\\\";

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldNotThrowErrorsOnNewUser() {
        Mockito.when(userRepository.isLoginExists(LOGIN)).thenReturn(false);

        NewUser user = NewUser.builder()
                .fullName(FULL_NAME)
                .login(LOGIN)
                .password(VALID_PASSWORD)
                .build();

        assertDoesNotThrow(() -> userValidator.validateNewUser(user));
    }

    @Test
    public void shouldThrowLoginExistsExceptionOnNewUser() {
        Mockito.when(userRepository.isLoginExists(LOGIN)).thenReturn(true);

        NewUser user = NewUser.builder()
                .fullName(FULL_NAME)
                .login(LOGIN)
                .password(VALID_PASSWORD)
                .build();

        assertThrows(LoginExistsException.class, () -> userValidator.validateNewUser(user));
    }

    @ParameterizedTest(name = "validateNewUser with password [{0}], should throw ConstraintViolationException with message [{1}]")
    @MethodSource("dataProvider")
    public void shouldThrowConstraintViolationException(String password, String exceptionMessage) {
        Mockito.when(userRepository.isLoginExists(LOGIN)).thenReturn(false);

        NewUser user = NewUser.builder()
                .fullName(FULL_NAME)
                .login(LOGIN)
                .password(password)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> userValidator.validateNewUser(user));

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }

    private static Stream<Arguments> dataProvider() {
        String exceptionMessage = "You have errors in you object";
        return Stream.of(
                Arguments.of(EMPTY_PASSWORD, exceptionMessage),
                Arguments.of(SHORT_PASSWORD, exceptionMessage),
                Arguments.of(LONG_PASSWORD, exceptionMessage),
                Arguments.of(NOT_MATCHING_REGEX_PASSWORD, exceptionMessage)
        );
    }
}
