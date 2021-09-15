package mk.ukim.finki.photography.service.impl;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.InvalidUsernameOrPasswordException;
import mk.ukim.finki.photography.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.photography.model.exceptions.UsernameNotFoundException;
import mk.ukim.finki.photography.model.exceptions.UsernameTaken;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return this.repository.findAll();
    }

    @Override
    public User save(User user) {
        return this.repository.save(user);
    }

    @Override
    public User findById(Long id) {
        // TODO make an exception for this
        return this.repository.findById(id).orElseThrow();
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        User user;
        if(this.repository.findByUsername(username).isPresent())
        {
            user = this.repository.findByUsername(username).orElseThrow();
        }
        else {
            user = null;
        }
        return user;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
    }


    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role role, String linkedin, String instagram, String facebook) {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()){
            throw new InvalidUsernameOrPasswordException();
        }
        if(!password.equals(repeatPassword)){
            throw new PasswordsDoNotMatchException();
        }
        if(this.repository.findByUsername(username).isPresent()){
            throw new UsernameTaken(username);
        }
        User user = new User(username, this.passwordEncoder.encode(password), name, surname, role, linkedin,  instagram, facebook);
        return repository.save(user);
    }

    @Transactional
    public void saveUserToDatabase(HttpServletRequest request,  Long id, String name, String surname, MultipartFile file, Role role, String username)  throws IOException {
        String fileName = file.getOriginalFilename();
        String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
        User user = this.repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setRole(role);
        user.setImageSrc(encodedImage);
        this.save(user);
    }

    @Override
    public void saveUserToDatabase(HttpServletRequest request, Long id, String name, String surname, Role role, String username) throws IOException {
        String authorUsername = request.getRemoteUser().toString();
        User user = this.repository.findByUsername(authorUsername).orElseThrow(() -> new UsernameNotFoundException(authorUsername));
        user.setName(name);
        user.setRole(role);
        user.setSurname(surname);
        user.setUsername(username);
        this.save(user);
    }

    @Override
    public Page<User> findPaginate(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage + pageSize;
        List<User> list;
        List<User> users = this.repository.findAll();
        if(users.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        Page<User> userPage = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), users.size());
        return userPage;
    }

    public List<User> findAllByUsernameNameSurname(String keyword){
        List<User> users;
        if(keyword!=null && !keyword.isEmpty()){
            users = this.repository.findAllByUsernameNameSurname(keyword);
        }
        else{
            users = this.repository.findAll();
        }
        return users;
    }


}
