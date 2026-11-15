package co.nz.westpac.bank.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

class AccountExceptionHandlerTest {

    private AccountExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new AccountExceptionHandler();
    }

    @Test
    void handleDatabaseConnectivity_shouldReturnBadRequestProblemDetail() {
        DatabaseAccessException ex = new DatabaseAccessException("Connection timed out");

        ProblemDetail result = exceptionHandler.handleDatabaseConnectivity(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Database connectivity issue");
        assertThat(result.getDetail()).isEqualTo("Connection timed out");
    }

    @Test
    void handleInvalidBankAccountNumber_shouldReturnBadRequestProblemDetail() {
        InvalidBankAccountException ex = new InvalidBankAccountException("Account format is invalid");

        ProblemDetail result = exceptionHandler.handleInvalidBankAccountNumber(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Invalid Bank account");
        assertThat(result.getDetail()).isEqualTo("Account format is invalid");
    }

    @Test
    void handleOffensiveNickname_shouldReturnBadRequestProblemDetail() {
        OffensiveNickNameException ex = new OffensiveNickNameException("Nickname contains prohibited words");

        ProblemDetail result = exceptionHandler.handleOffensiveNickname(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Offensive Nickname");
        assertThat(result.getDetail()).isEqualTo("Nickname contains prohibited words");
    }

    @Test
    void handleMaximumAccountsCreated_shouldReturnBadRequestProblemDetail() {
        MaximumAccountsException ex = new MaximumAccountsException("Limit of 5 accounts reached");

        ProblemDetail result = exceptionHandler.handleMaximumAccountsCreated(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Maximum account count exceeded for this name and nick name pair");
        assertThat(result.getDetail()).isEqualTo("Limit of 5 accounts reached");
    }

    @Test
    @SuppressWarnings("unchecked")
    void handleConstraintViolation_shouldExtractFieldNamesAndReturnErrorsMap() {
        // Mock nested property path (e.g., "createAccount.nickname" -> "nickname")
        ConstraintViolation<?> nestedViolation = mock(ConstraintViolation.class);
        Path nestedPath = mock(Path.class);
        when(nestedPath.toString()).thenReturn("createAccount.nickname");
        when(nestedViolation.getPropertyPath()).thenReturn(nestedPath);
        when(nestedViolation.getMessage()).thenReturn("must not be blank");

        // Mock simple property path (e.g., "accountNumber" -> "accountNumber")
        ConstraintViolation<?> simpleViolation = mock(ConstraintViolation.class);
        Path simplePath = mock(Path.class);
        when(simplePath.toString()).thenReturn("accountNumber");
        when(simpleViolation.getPropertyPath()).thenReturn(simplePath);
        when(simpleViolation.getMessage()).thenReturn("invalid length");

        ConstraintViolationException ex = new ConstraintViolationException(
                Set.of(nestedViolation, simpleViolation));

        ProblemDetail result = exceptionHandler.handleConstraintViolations(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Constraint Violation");
        assertThat(result.getDetail()).isEqualTo("One or more parameters failed validation.");

        assertThat(result.getProperties()).containsKey("errors");
        Map<String, String> errors = (Map<String, String>) result.getProperties().get("errors");
        assertThat(errors)
                .hasSize(2)
                .containsEntry("nickname", "must not be blank")
                .containsEntry("accountNumber", "invalid length");
    }
}
