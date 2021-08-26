package mk.ukim.finki.photography.model.exceptions;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String username){
        super(String.format("The username %s is not found", username));
    }
}
