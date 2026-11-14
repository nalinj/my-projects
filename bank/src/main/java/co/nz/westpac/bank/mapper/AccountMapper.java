package co.nz.westpac.bank.mapper;

import org.mapstruct.Mapper;

import co.nz.westpac.bank.entity.Account;
import co.nz.westpac.bank.vo.AccountVO;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	AccountVO mapToVO (Account account);
	
	Account mapToEntity (AccountVO accountVO);
}
