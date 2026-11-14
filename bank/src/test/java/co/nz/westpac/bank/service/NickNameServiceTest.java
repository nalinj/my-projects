package co.nz.westpac.bank.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.nz.westpac.bank.repository.NickNameRepository;

@ExtendWith(MockitoExtension.class)
class NickNameServiceTest {

    @Mock
    private NickNameRepository nickNameRepository;

    @InjectMocks
    private NickNameService nickNameService;

    @Test
    void existsByName_ShouldReturnTrue_WhenNickNameExistsInRepository() {
        // Arrange
        String nickName = "BadWord";
        when(nickNameRepository.existsByName(nickName)).thenReturn(true);

        // Act
        boolean result = nickNameService.existsByName(nickName);

        // Assert
        assertTrue(result);
        verify(nickNameRepository).existsByName(nickName);
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNickNameDoesNotExistInRepository() {
        // Arrange
        String nickName = "Savings";
        when(nickNameRepository.existsByName(nickName)).thenReturn(false);

        // Act
        boolean result = nickNameService.existsByName(nickName);

        // Assert
        assertFalse(result);
        verify(nickNameRepository).existsByName(nickName);
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNickNameIsNull() {
        // Arrange
        when(nickNameRepository.existsByName(null)).thenReturn(false);

        // Act
        boolean result = nickNameService.existsByName(null);

        // Assert
        assertFalse(result);
        verify(nickNameRepository).existsByName(null);
    }
}
