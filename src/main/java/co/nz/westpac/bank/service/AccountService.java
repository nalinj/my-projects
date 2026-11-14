package co.nz.westpac.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import co.nz.westpac.bank.co.AccountVO;
import co.nz.westpac.bank.entity.Account;
import co.nz.westpac.bank.exception.DatabaseAccessException;
import co.nz.westpac.bank.exception.InvalidBankAccountException;
import co.nz.westpac.bank.exception.MaximumAccountsException;
import co.nz.westpac.bank.exception.OffensiveNickNameException;
import co.nz.westpac.bank.mapper.AccountMapper;
import co.nz.westpac.bank.repository.AccountRepository;
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
	
	@Transactional
	public AccountVO create(AccountVO accountVO) throws MaximumAccountsException {
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
	
	public AccountVO getAccount(String accountNumber) {
		Account account = accountRepository.findByNumber(accountNumber);
		if (account == null) {
			throw new InvalidBankAccountException("Invalid bank account number");
		}
		
		return accountMapper.mapToVO(account);
	}

}
