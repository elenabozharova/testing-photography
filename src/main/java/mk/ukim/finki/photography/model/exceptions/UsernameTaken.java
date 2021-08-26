package mk.ukim.finki.photography.model.exceptions;

public class UsernameTaken extends RuntimeException {
    public UsernameTaken(String username){
        super(String.format("The username %s already exists", username));
    }
}
