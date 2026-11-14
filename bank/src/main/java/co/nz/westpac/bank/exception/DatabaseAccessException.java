package co.nz.westpac.bank.exception;

import java.io.Serial;

public class DatabaseAccessException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public DatabaseAccessException(String message) {
		super(message);
	}

}
