package mk.ukim.finki.photography.register;

import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.InvalidUsernameOrPasswordException;
import mk.ukim.finki.photography.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.photography.model.exceptions.UsernameTaken;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RegisterUnitTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl service;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        User user = new User("username",
                "password",
                "name",
                "surname",
                Role.ROLE_USER,
                "",
                "",
                "");

        Mockito.when(this.userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
        Mockito.when(this.userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        Mockito.when(this.userRepository.findByUsername("user123")).thenReturn(Optional.of(user));
        service = Mockito.spy(new UserServiceImpl(userRepository, passwordEncoder));

    }
    /**
     * The register method in the UserServiceImpl class is tested using Graph source code based approach.
     * Input params: String username, String password, String repeatPassword, String name, String surname, Role role,
     *               String linkedin, String instagram, String facebook
     * Coverage criteria : Edge coverage (graph generated manually)
     * Test Requirements: (1,2), (1,3), (3,4) , (3,5) , (5,6) , (5,7)
     * Test Paths: [1,2] , [1,3,4] , [1,3,5,6] , [1,3,5,7]
     * */

    /**
     * Test path 1 -> [1,2]
     * */
    @Test
    public void testRegisterT1(){
        // if(username == null || username.isEmpty() || password == null || password.isEmpty()) == true
        Assert.assertThrows("InvalidUsernameOrPasswordException expected", InvalidUsernameOrPasswordException.class, () -> service.register("", "password", "password", "name","surname", Role.ROLE_USER, "","", ""));
    }

    /** Test path 2 -> [1, 3, 4 ]
     * */
    @Test
    public void testRegisterT2(){
        // if(username == null || username.isEmpty() || password == null || password.isEmpty()) == false
        // if(!password.equals(repeatPassword)) == true
        Assert.assertThrows("PasswordsDoNotMatchException expected", PasswordsDoNotMatchException.class, () -> service.register("username", "password", "passwordOther", "name","surname", Role.ROLE_USER, "","", ""));
    }

    /** Test path 3 -> [1,3,5,6]
     * */
    @Test
    public void testRegisterT3(){
        // if(username == null || username.isEmpty() || password == null || password.isEmpty()) == false
        // if(!password.equals(repeatPassword)) == false
        // if(this.repository.findByUsername(username).isPresent()) == true
        Assert.assertThrows("UsernameTaken exception expected", UsernameTaken.class, () -> service.register("user123","password", "password", "name","surname", Role.ROLE_USER, "","", ""));
    }

    /** Test path 4 -> [1,3, 5, 7]
     * */
    @Test
    public void testRegisterT4() {
        // if(username == null || username.isEmpty() || password == null || password.isEmpty()) == false
        // if(!password.equals(repeatPassword)) == false
        // if(this.repository.findByUsername(username).isPresent()) == false
        User result = service.register("username", "password", "password","name", "surname", Role.ROLE_USER, "", "","");
        Assert.assertEquals("username", result.getUsername());
        Assert.assertEquals("password", result.getPassword());
        Assert.assertEquals("name", result.getName());
        Assert.assertEquals("surname", result.getSurname());
        Assert.assertEquals("", result.getLinkedin());
        Assert.assertEquals("", result.getInstagram());
        Assert.assertEquals("", result.getFacebook());
    }

    @Test
    public void testNullUsername()
    {
        String username = null;
        String password = "password";
        Assert.assertThrows("Invalid Username or password exception expected", InvalidUsernameOrPasswordException.class, () -> this.service.register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" ));
        Mockito.verify(this.service).register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" );
    }

    @Test
    public void testEmptyUsername()
    {
        String username = "";
        String password = "password";
        Assert.assertThrows("Invalid Username or password exception expected", InvalidUsernameOrPasswordException.class, () -> this.service.register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" ));
        Mockito.verify(this.service).register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" );
    }

    @Test
    public void testNullPassword()
    {
        String username = "username";
        String password = null;
        Assert.assertThrows("Invalid Username or password exception expected", InvalidUsernameOrPasswordException.class, () -> this.service.register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" ));
        Mockito.verify(this.service).register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" );
    }

    @Test
    public void testEmptyPassword()
    {
        String username = "username";
        String password = "";
        Assert.assertThrows("Invalid Username or password exception expected", InvalidUsernameOrPasswordException.class, () -> this.service.register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" ));
        Mockito.verify(this.service).register(username, password, "password", "name", "surname", Role.ROLE_USER, "linkedin username","instagram username", "facebook username" );
    }

    @Test
    public void testPasswordsNotMatch()
    {
        String username = "username";
        String password = "password";
        String notSamePassword = "otherPassword";
        Assert.assertThrows("Passwords do not match exception expected", PasswordsDoNotMatchException.class, () -> this.service.register(username, password, notSamePassword, "name", "surname", Role.ROLE_USER, "linkedin", "facebook", "instagram" ));
        Mockito.verify(this.service).register(username, password, notSamePassword, "name", "surname", Role.ROLE_USER, "linkedin", "facebook", "instagram" );
    }

    @Test
    public void testDuplicateUsername()
    {
        String username = "test";
        String password = "password";
        Assert.assertThrows("Username taken exception expected", UsernameTaken.class, () -> this.service.register(username, password, password, "name", "surname", Role.ROLE_USER, "linkedin", "facebook", "instagram" ));
        Mockito.verify(this.service).register(username, password, password, "name", "surname", Role.ROLE_USER, "linkedin", "facebook", "instagram" );
    }



}
