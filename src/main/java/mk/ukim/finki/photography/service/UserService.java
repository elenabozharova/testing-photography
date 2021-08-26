package mk.ukim.finki.photography.service;

import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    User save(User user);

    User findById(Long id);

    User findByUsername(String username);

    UserDetails loadUserByUsername(String username);

    User register(String username, String password, String repeatPassword, String name, String surname, Role role, String linkedin, String instagram, String facebook);

    public void saveUserToDatabase(HttpServletRequest request,  Long id, String name, String surname, MultipartFile file, Role role, String username) throws IOException;

    public void saveUserToDatabase(HttpServletRequest request,  Long id, String name, String surname,Role role, String username) throws IOException;
                                   public Page<User> findPaginate(Pageable pageable);

    List<User> findAllByUsernameNameSurname(String username);
}
