package mk.ukim.finki.photography.user;


import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.UsernameNotFoundException;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.UserServiceImpl;
import org.h2.server.web.WebApp;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.imageio.stream.FileImageInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserUploadTests {
/** * * * **
 * In this test I am testing the methods in the UserServiceImpl class
 *  where there is a file upload
 *
 * * */
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl service;


    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        // for when the username does not exist
        Mockito.when(this.userRepository.findByUsername("test")).thenReturn(null);
        service = Mockito.spy(new UserServiceImpl(userRepository, passwordEncoder));
    }

    // should throw Exception
    @Test
    public void testSaveUserToDatabaseError() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser("test"); // this user does not exist in the test database
        File image = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\camera.png");
        FileInputStream fis = new FileInputStream(image);
        MockMultipartFile file = new MockMultipartFile("file", fis );
        String authorName = request.getRemoteUser();
        Assert.assertThrows("Null pointer exception should be thrown", NullPointerException.class, () -> this.service.saveUserToDatabase(request, Long.valueOf(14523), "name","surname",file, Role.ROLE_USER, authorName));
    }


    @Test
    public void testSaveUserToDatabaseWithImage() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // Add a user in the test database
        User user = new User("username", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        user = service.save(user);
        request.setRemoteUser("username");

        File image = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\camera.png");
        FileInputStream fis = new FileInputStream(image);
        MockMultipartFile picture = new MockMultipartFile("file", "picture1.jpg", "multipart/form-data", fis);
        String authorName = request.getRemoteUser();

        try {
            this.service.saveUserToDatabase(request, Long.valueOf(14523), "name", "surname", picture, Role.ROLE_USER, authorName);
        }
        catch (Exception e)
        {
           fail("Should not have thrown any exception");
        }
    }
}
