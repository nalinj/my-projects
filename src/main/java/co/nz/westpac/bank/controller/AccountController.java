package co.nz.westpac.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.nz.westpac.bank.co.AccountVO;
import co.nz.westpac.bank.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/create")
	@ResponseBody()
	public ResponseEntity<AccountVO> createAccount(@RequestBody AccountVO accountVO) {
		return new ResponseEntity<>(accountService.create(accountVO), HttpStatus.CREATED);
	}
	
	@GetMapping("/details")
	public ResponseEntity<AccountVO> accountDetails(@RequestBody AccountVO accountVO){
		return new ResponseEntity<>(accountService.getAccount(accountVO.getNumber()), HttpStatus.OK);
	}
}
