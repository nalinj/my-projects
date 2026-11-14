package co.nz.westpac.bank.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import co.nz.westpac.bank.repository.NickNameRepository;

@Service
public class NickNameService {
	
	private final NickNameRepository nickNameRepository;
	
	public NickNameService(NickNameRepository nickNameRepository) {
		this.nickNameRepository = nickNameRepository;
	}
	
	//TODO: ECache is used here but industry standard cache (e.g. Redis) needs to be used for production.
	@Cacheable(condition = "#nickName != null")
	public boolean existsByName(String nickName) {
		return nickNameRepository.existsByName(nickName);
	}

}
