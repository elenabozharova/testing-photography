package mk.ukim.finki.photography.model.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(){
        super("Invalid credentials");
    }
}
