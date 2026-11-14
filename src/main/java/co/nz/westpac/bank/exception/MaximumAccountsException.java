package co.nz.westpac.bank.exception;

import java.io.Serial;

public class MaximumAccountsException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public MaximumAccountsException(String message) {
		super(message);
	}

}
