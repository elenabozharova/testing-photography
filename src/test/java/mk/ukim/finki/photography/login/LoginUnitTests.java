package mk.ukim.finki.photography.login;

import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.AuthServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTests {
    /**
     * Tests for the AuthServiceImpl class with mock of the userRepository behaviour.
     * */

    @Mock
    private UserRepository userRepository;


    private AuthServiceImpl service;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        User user = new User("username", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        Mockito.when(this.userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));
        Mockito.when(this.userRepository.findByUsernameAndPassword(null, null)).thenReturn(Optional.empty());
        service = Mockito.spy(new AuthServiceImpl(userRepository));
    }

    /**
     * Tests for the login method using ISP interface based and graph edge coverage.
     * Characteristics:
     *       C1 - Username is null
     *          b1 - true
     *          b2 - false
     *       C2 - Password is null
     *          b1 - true
     *          b2 - false
     *
     *    Test criterion:   Each choice coverage
     *       T1 - C1 true, C2 true
     *       T2 - C1 false, C2 false
     * */

    /**
     * T1
     * */
    @Test
    public void testLoginT1(){
        User user = this.service.login("username", "password");

        // verification
        Mockito.verify(this.service).login("username", "password");

        Assert.assertEquals("Username does not match","username", user.getUsername());
        Assert.assertEquals("Password does not match", "password", user.getPassword());
    }

    /**
     * T2
     * */

    @Test
    public void testLoginT2(){
        User user = this.service.login(null, null);

        // verification
        Mockito.verify(this.service).login("username", "password");
    }

    @Test
    public void testUsernameNull()
    {
        String username = null;
        String password = "password";
        Assert.assertThrows("Invalid Arguments exception expected", InvalidArgumentsException.class, () -> service.login(username, password));
        Mockito.verify(this.service).login(username, password);
    }

    @Test
    public void testUsernameEmpty()
    {
        String username = "";
        String password = "password";
        Assert.assertThrows("Invalid Arguments exception expected", InvalidArgumentsException.class, () -> service.login(username, password));
        Mockito.verify(this.service).login(username, password);
    }

    @Test
    public void testPasswordNull()
    {
        String username = "username";
        String password = null;
        Assert.assertThrows("Invalid Arguments exception expected", InvalidArgumentsException.class, () -> service.login(username, password));
        Mockito.verify(this.service).login(username, password);
    }

    @Test
    public void testPasswordEmpty()
    {
        String username = "username";
        String password = "";
        Assert.assertThrows("Invalid Arguments exception expected", InvalidArgumentsException.class, () -> service.login(username, password));
        Mockito.verify(this.service).login(username, password);
    }



}
