package co.nz.westpac.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.nz.westpac.bank.entity.Account;
import co.nz.westpac.bank.exception.DatabaseAccessException;
import co.nz.westpac.bank.exception.MaximumAccountsException;
import co.nz.westpac.bank.exception.OffensiveNickNameException;
import co.nz.westpac.bank.mapper.AccountMapper;
import co.nz.westpac.bank.repository.AccountRepository;
import co.nz.westpac.bank.vo.AccountVO;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NickNameService nickNameService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private AccountService accountService;

    private AccountVO inputVO;
    private Account mappedEntity;
    private Account savedEntity;
    private AccountVO expectedOutputVO;

    @BeforeEach
    void setUp() {
        inputVO = new AccountVO();
        inputVO.setName("John Doe");
        inputVO.setNickName("Savings");

        mappedEntity = new Account();
        savedEntity = new Account();

        expectedOutputVO = new AccountVO();
        expectedOutputVO.setName("John Doe");
        expectedOutputVO.setNickName("Savings");
    }

    @Test
    void create_ShouldReturnSavedAccountVO_WhenValidRequest() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenReturn(connection);
        when(accountRepository.countByNameAndNickName("John Doe", "Savings")).thenReturn(5L); // Boundary check (<= 5)
        when(nickNameService.existsByName("Savings")).thenReturn(false);
        when(accountMapper.mapToEntity(inputVO)).thenReturn(mappedEntity);
        when(accountRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(accountMapper.mapToVO(savedEntity)).thenReturn(expectedOutputVO);

        // Act
        AccountVO result = accountService.create(inputVO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("Savings", result.getNickName());
        verify(accountRepository).save(mappedEntity);
        verify(connection).close(); // Ensures try-with-resources closed the connection
    }

    @Test
    void create_ShouldThrowMaximumAccountsException_WhenAccountCountExceedsFive() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenReturn(connection);
        when(accountRepository.countByNameAndNickName("John Doe", "Savings")).thenReturn(6L);

        // Act & Assert
        MaximumAccountsException exception = assertThrows(
                MaximumAccountsException.class,
                () -> accountService.create(inputVO)
        );
        assertEquals("Maximum number of accounts has reached for this customer", exception.getMessage());

        // Verify downstream calls are not made
        verify(nickNameService, never()).existsByName(any());
        verify(accountRepository, never()).save(any());
        verify(connection).close();
    }

    @Test
    void create_ShouldThrowOffensiveNickNameException_WhenNickNameExists() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenReturn(connection);
        when(accountRepository.countByNameAndNickName("John Doe", "Savings")).thenReturn(2L);
        when(nickNameService.existsByName("Savings")).thenReturn(true);

        // Act & Assert
        OffensiveNickNameException exception = assertThrows(
                OffensiveNickNameException.class,
                () -> accountService.create(inputVO)
        );
        assertEquals("Nickname entered is offensive", exception.getMessage());

        // Verify save is never called
        verify(accountRepository, never()).save(any());
        verify(connection).close();
    }

    @Test
    void create_ShouldThrowDatabaseAccessException_WhenSQLExceptionOccurs() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Act & Assert
        DatabaseAccessException exception = assertThrows(
                DatabaseAccessException.class,
                () -> accountService.create(inputVO)
        );
        assertEquals("Database is unavailable", exception.getMessage());

        verify(accountRepository, never()).countByNameAndNickName(any(), any());
        verify(accountRepository, never()).save(any());
    }
}
