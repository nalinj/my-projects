package co.nz.westpac.bank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import co.nz.westpac.bank.repository.NickNameRepository;
import co.nz.westpac.bank.service.AccountService;
import co.nz.westpac.bank.vo.AccountVO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AccountService accountService;

	@MockitoBean
	private NickNameRepository nickNameRepository;

	@MockitoBean
	private CacheManager cacheManager;
	
	private AccountVO sampleAccount;

	@BeforeEach
	void setUp() {
		sampleAccount = new AccountVO();
		sampleAccount.setNumber("12345678");
	}

	@Test
	void createAccount_ReturnsCreatedStatusAndAccount() throws Exception {
		when(accountService.create(any(AccountVO.class))).thenReturn(sampleAccount);

		mockMvc.perform(post("/account/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sampleAccount)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.number").value("12345678"));

		verify(accountService).create(any(AccountVO.class));
	}

	@Test
	void accountDetails_ReturnsOkStatusAndAccount() throws Exception {
		when(accountService.getAccount("12345678")).thenReturn(sampleAccount);

		mockMvc.perform(get("/account/details")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sampleAccount)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.number").value("12345678"));

		verify(accountService).getAccount("12345678");
	}
}

