package co.nz.westpac.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import co.nz.westpac.bank.entity.Account;
import co.nz.westpac.bank.exception.DatabaseAccessException;
import co.nz.westpac.bank.exception.InvalidBankAccountException;
import co.nz.westpac.bank.exception.MaximumAccountsException;
import co.nz.westpac.bank.exception.OffensiveNickNameException;
import co.nz.westpac.bank.mapper.AccountMapper;
import co.nz.westpac.bank.repository.AccountRepository;
import co.nz.westpac.bank.vo.AccountVO;
import jakarta.transaction.Transactional;

@Service
public class AccountService {
	
	private final AccountRepository accountRepository;
	
	private final NickNameService nickNameService;
	
	private final AccountMapper accountMapper;
	
	private final DataSource dataSource;
	
	public AccountService(AccountRepository accountRepository, NickNameService nickNameService, AccountMapper accountMapper, DataSource dataSource) {
		this.accountRepository = accountRepository;
		this.nickNameService = nickNameService;
		this.accountMapper = accountMapper;
		this.dataSource = dataSource;
	}
	
	/**
	 * Creates an account.
	 * 
	 * @param accountVO
	 * @return the new account created
	 */
	@Transactional
	public AccountVO create(AccountVO accountVO) {
		try (Connection connection = dataSource.getConnection()) {
			long accountCount = accountRepository.countByNameAndNickName(accountVO.getName(), accountVO.getNickName());
			
			if (accountCount > 5) {
				throw new MaximumAccountsException("Maximum number of accounts has reached for this customer");
			}
			
			boolean nickNameExists = nickNameService.existsByName(accountVO.getNickName());
			
			if (nickNameExists) {
				throw new OffensiveNickNameException("Nickname entered is offensive");
			}
			
			return accountMapper.mapToVO(accountRepository.save(accountMapper.mapToEntity(accountVO)));
        } catch (SQLException e) {
            throw new DatabaseAccessException("Database is unavailable");
        }
	}
	
	/**
	 * Returns the account by account number.
	 * 
	 * @param accountNumber
	 * @return
	 */
	public AccountVO getAccount(String accountNumber) {
		if (accountNumber == null || accountNumber.isBlank() || accountNumber.isEmpty()) {
			throw new InvalidBankAccountException("Invalid bank account number");
		}
		
		Account account = accountRepository.findByNumber(accountNumber);
		
		if (account == null) {
			throw new InvalidBankAccountException("Account doesn't exist");
		}
		
		return accountMapper.mapToVO(account);
	}

}
