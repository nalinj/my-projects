package co.nz.westpac.bank.co;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class AccountVO implements Serializable {


	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String number;
	
	private String name;
	
	private String nickName;
}
