package mk.ukim.finki.photography.service;

import mk.ukim.finki.photography.model.User;

public interface AuthService {
    User login(String username, String password);
}
