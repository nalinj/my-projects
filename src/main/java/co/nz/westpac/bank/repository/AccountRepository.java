package co.nz.westpac.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.nz.westpac.bank.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	public long countByNameAndNickName(String name, String nickName);

	public Account findByNumber(String number);
}
