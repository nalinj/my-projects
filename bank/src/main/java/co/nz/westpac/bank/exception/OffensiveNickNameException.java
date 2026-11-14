package co.nz.westpac.bank.exception;

import java.io.Serial;

public class OffensiveNickNameException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public OffensiveNickNameException(String message) {
		super(message);
	}
	
}
