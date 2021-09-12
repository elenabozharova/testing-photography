package mk.ukim.finki.photography.user;

import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl service;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);

        User user1 = new User("user", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user1.setId((long)976);

        User user2 = new User("user.name", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user2.setId((long)798);

        User user3 = new User("user.name", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user3.setId((long)764);

        List<User> listAll = new ArrayList<User>();
        listAll.add(user1);
        listAll.add(user2);
        listAll.add(user3);

        List<User> listFindByNameAndSurname = new ArrayList<>();
        listFindByNameAndSurname.add(user1);
        listFindByNameAndSurname.add(user2);

        Mockito.when(userRepository.findById((long)976)).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findAll()).thenReturn(listAll);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
        Mockito.when(userRepository.save(null)).thenReturn(null);
        Mockito.when(userRepository.findAllByUsernameNameSurname("Jane")).thenReturn(listFindByNameAndSurname);

        service = Mockito.spy(new UserServiceImpl(userRepository, passwordEncoder));
    }

    /**
     * The user repository will return 3 users.
     * There are no parameters in the method, and no branching, so I tested only the call.
     * */
    @Test
    public void testListAll()
    {
        List<User> users = this.service.findAll();
        Mockito.verify(this.service).findAll();
        Assert.assertEquals(3, users.size());
    }

    /**
     *  This method is tested with ISP method.
     *  The public User save(User user) method has only one parameter.
     *  C1 = user is null
     *      b1 - true
     *      b2 - false
     * */

    /**
     * C1 = true
     * */
    @Test
    public void testSaveUserNotNull()
    {
        User user = new User("user", "password", "name", "surname", Role.ROLE_USER, "","", "");
        User user2 = this.service.save(user);
        Mockito.verify(this.service).save(user);
        Assert.assertNotNull("User was null", user2);
        Assert.assertEquals("user", user2.getUsername());
        Assert.assertEquals("password", user2.getPassword());
        Assert.assertEquals("name", user2.getName());
        Assert.assertEquals("surname", user2.getSurname());
        Assert.assertEquals(Role.ROLE_USER, user2.getRole());
        Assert.assertEquals("", user2.getLinkedin());
        Assert.assertEquals("", user2.getFacebook());
        Assert.assertEquals("", user2.getInstagram());
    }

    /**
     * C1 = false
     * */
    @Test
    public void testSaveUserNull()
    {
        User user = this.service.save(null);
        Mockito.verify(service).save(null);
        Assert.assertNull(user);
    }


    /**
     * This method is tested with ISP.
     * Characteristics:
     * C1 - range of Id value
     *      b1 - < 0
     *      b2 - = 0
     *      b3 - > 0
     *
     * */
    /**
     * b1 - Id < 0
     * */
    @Test
    public void testFindUserByIdLessThen0() {
        Assert.assertThrows("NoSuchElement exception expected", NoSuchElementException.class, () -> this.service.findById((long)-145));
        Mockito.verify(service).findById((long)-145);
    }

    /**
     * b2 - Id = 0
     * */
    @Test
    public void testFindUserByIdEqualTo0() {
        Assert.assertThrows("NoSuchElement exception expected", NoSuchElementException.class, ()-> this.service.findById((long)0));
        Mockito.verify(service).findById((long)0);
    }

    /**
     * b3 - Id > 0
     * */
    @Test
    public void testFindUserById()
    {
        User user = service.findById((long)976);
        Mockito.verify(service).findById((long)976);
        Assert.assertEquals("user", user.getUsername());
        Assert.assertEquals("password", user.getPassword());
        Assert.assertEquals("name", user.getName());
        Assert.assertEquals("surname", user.getSurname());
        Assert.assertEquals(Role.ROLE_USER, user.getRole());
        Assert.assertEquals("", user.getLinkedin());
        Assert.assertEquals("", user.getFacebook());
        Assert.assertEquals("", user.getInstagram());
    }

    /**
     * The findByUsername(string Username) method is tested with ISP.
     * Characteristics:
     *      C1 - Username is null
     *          b1 - true
     *          b2 - false
     * */

    /**
     * b1 - Username is null
     * */
    @Test
    public void testFindByUsernameNull()
    {
        User user  = service.findByUsername(null);
        Assert.assertNull(user);
        Mockito.verify(service).findByUsername(null);
    }

    /**
     * b2 - Username is not null
     * */
    @Test
    public void testFindByUsernameNotNull()
    {
        User result = service.findByUsername("user");

        Mockito.verify(service).findByUsername("user");
        // check all parameters
        Assert.assertEquals("user", result.getUsername());
        Assert.assertEquals("password", result.getPassword());
        Assert.assertEquals("surname", result.getSurname());
        Assert.assertEquals(Role.ROLE_USER, result.getRole());
        Assert.assertEquals("", result.getFacebook());
        Assert.assertEquals("", result.getInstagram());
        Assert.assertEquals("", result.getLinkedin());
        Assert.assertNotNull(result);
    }

    /**
     * If the username is present, but the user is not found in the database.
     * */
    @Test
    public void testFindByUsernameNotFound()
    {
        User result = service.findByUsername("username"); // this does not exist and is not initialized in the mocks
        Mockito.verify(service).findByUsername("username");
        Assert.assertNull(result);
    }

    /**
     * The method findAllByUsernameAndSurname(String keyword) is tested with ISP.
     * Characteristics:
     *      C1 - keyword is null
     *          b1 - true
     *          b2 - false
     * */

    /**
     * b1 - Keyword is null
     * */
    @Test
    public void testFindAllByUsernameAndSurnameKeywordNull()
    {
        List<User> users = this.service.findAllByUsernameNameSurname(null);
        Mockito.verify(service).findAllByUsernameNameSurname(null);
        Assert.assertEquals(3, users.size()); // the list of all users
    }

    /**
     * b2 - Keyword is not null
     * */
    @Test
    public void testFindAllByUsernameAndSurnameKeywordNotNull()
    {
        assertThat(service.findAllByUsernameNameSurname("Jane").size()).isEqualTo(2);
        Mockito.verify(service).findAllByUsernameNameSurname("Jane");
    }

    @Test
    public void testFindAllByUsernameAndSurnameKeywordEmpty()
    {
        // find all method will be called instead
        List<User> users = this.service.findAllByUsernameNameSurname("");
        Mockito.verify(service).findAllByUsernameNameSurname("");
        Assert.assertEquals(3, users.size()); // the list of all users
    }

}
