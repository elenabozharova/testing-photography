package mk.ukim.finki.photography.model.exceptions;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(){
        super("Invalid username/password");
    }
}
