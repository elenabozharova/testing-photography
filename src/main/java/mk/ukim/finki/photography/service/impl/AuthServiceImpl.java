package mk.ukim.finki.photography.service.impl;

import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.photography.model.exceptions.InvalidCredentialsException;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidCredentialsException::new);
    }
}
