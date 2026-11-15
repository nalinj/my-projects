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
	
	/**
	 * Checks the nick name for offensive names ignoring case. If the nick name is empty it is not offensive.
	 * 
	 * @param nickName nick name
	 * @return true if in the offensive nickname list else false.
	 */
	//TODO: ECache is used here but industry standard cache (e.g. Redis) needs to be used for production.
	@Cacheable(value = "offensiveNickNames", key = "#nickName", condition = "#nickName != null")
	public boolean existsByName(String nickName) {
		if (nickName == null || nickName.isEmpty() || nickName.isBlank()) {
			return false;
		}
		return nickNameRepository.existsByNameIgnoreCase(nickName);
	}

}
