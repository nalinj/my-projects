package co.nz.westpac.bank.exception;

import java.io.Serial;

public class InvalidBankAccountException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public InvalidBankAccountException(String message) {
		super(message);
	}

}
