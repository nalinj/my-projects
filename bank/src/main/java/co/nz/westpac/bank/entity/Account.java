package co.nz.westpac.bank.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Account implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 16, max = 16, message = "Account number needs to be 16 characters")
	@Column(name = "number", unique = true, nullable = false, updatable = false)
	private String number;
	
	@NotNull(message = "Name is mandatory")
	@Column(nullable = false)
	private String name;
	
	@Size(min = 5, max = 30, message = "Nickname must be between 5 and 30 characters")
	@Column
	private String nickName;

    @PrePersist
    protected void onCreate() {
        if (this.number == null) {
            this.number = generateAccountNumber();
        }
    }

    //TODO: uniqueness of account numbers needs to be assured
    private String generateAccountNumber() {
        // Example: 16-digit random numeric string
        long number = ThreadLocalRandom.current().nextLong(1000_0000_0000_0000L, 1_0000_0000_0000_0000L);
        return String.valueOf(number);
    }
}
