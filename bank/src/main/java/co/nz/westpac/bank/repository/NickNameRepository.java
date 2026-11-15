package co.nz.westpac.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.nz.westpac.bank.entity.NickName;

public interface NickNameRepository extends JpaRepository<NickName, Long>{

	boolean existsByNameIgnoreCase(String nickName);
}
