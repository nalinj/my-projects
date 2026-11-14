package co.nz.westpac.bank.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class AccountExceptionHandler {
	
    @ExceptionHandler(DatabaseAccessException.class)
    public ProblemDetail handleDatabaseConnectivity(DatabaseAccessException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Database connectivity issue");
        return problem;
    }
    
    @ExceptionHandler(InvalidBankAccountException.class)
    public ProblemDetail handleInvalidBankAccountNumber(InvalidBankAccountException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Invalid Bank account");
        return problem;
    }
    
    @ExceptionHandler(OffensiveNickNameException.class)
    public ProblemDetail handleOffensiveNickname(OffensiveNickNameException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Offensive Nickname");
        return problem;
    }
	
    @ExceptionHandler(MaximumAccountsException.class)
    public ProblemDetail handleMaximumAccountsCreated(MaximumAccountsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Maximum account count exceeded for this name and nick name pair");
        return problem;
    }
    
    // 2. Handle Bean Validation Errors (@Valid / @NotNull / @Size)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "One or more parameters failed validation.");
        problem.setTitle("Constraint Violation");

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.contains(".") 
                    ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1) 
                    : propertyPath;
                    
            errors.put(fieldName, violation.getMessage());
        });

        problem.setProperty("errors", errors);
        return problem;
    }

}
