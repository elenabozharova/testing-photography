package mk.ukim.finki.photography.user;

import mk.ukim.finki.photography.config.TestConfiguration;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserIntegrationTests {

    MockMvc mockMvc;

    @Autowired
    public UserService userService;

    private static User user;
    public static  boolean dataInitialized = false;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        initData();
    }

    public void initData(){
        if(!dataInitialized)
        {
            User user = new User("username", "password", "name", "surname", Role.ROLE_USER, "", "","");

            userService.save(user);
            dataInitialized = true;
        }
    }

    @Test
    public void testShowListTest() throws Exception {
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(usersRequest).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.model().attributeExists("users")).
                andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "users")).
                andExpect(MockMvcResultMatchers.view().name("master"));

    }

    @Test
    public void testGetUser() throws Exception{
        User user = this.userService.findByUsername("username");
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.get("/users/" + user.getId());
        this.mockMvc.perform(usersRequest).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.model().attributeExists("user")).
                andExpect(MockMvcResultMatchers.model().attributeExists("photos")).
                andExpect(MockMvcResultMatchers.model().attributeExists("followers")).
                andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "user")).
                andExpect(MockMvcResultMatchers.view().name("master"));
    }



    @WithMockUser(value = "test", password = "pass")
    @Test
    public void testGetUserByUsernameTest() throws Exception {
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.get("/users/profile/username");
        this.mockMvc.perform(userRequest)
                    .andDo(MockMvcResultHandlers.print()).
                    andExpect(MockMvcResultMatchers.status().isOk()).
                    andExpect(MockMvcResultMatchers.model().attributeExists("user")).
                    andExpect(MockMvcResultMatchers.model().attributeExists("photos")).
                    andExpect(MockMvcResultMatchers.model().attributeExists("followers")).
                    andExpect(MockMvcResultMatchers.model().attributeExists("following")).
                    andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "user")).
                    andExpect(MockMvcResultMatchers.view().name("master"));

    }

    @Test
    public void getEditTest() throws Exception {
        // must have a user in the database
        User user = this.userService.findByUsername("username");
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.get("/users/edit/" + user.getId() );
        this.mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.model().attributeExists("user")).
                andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "edituser")).
                andExpect(MockMvcResultMatchers.view().name("master"));

    }

    @Test
    @WithMockUser(username = "username", password = "password", roles = "USER")
    public void editProfile() throws Exception {
        User user = this.userService.findByUsername("username");
        String username = "username";
        String name = "name";
        String surname = "surname";
        File image = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\default_picture.jpg");
        FileInputStream fis = new FileInputStream(image);
        MockMultipartFile profilePicture = new MockMultipartFile("profilePicture", "profilePicture","image/png" , fis);

        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.multipart("/users/edit/" + user.getId()).file(profilePicture).
                param("username", username).
                param("name", name).
                param("surname", surname).
                param("role", Role.ROLE_USER.toString());

        this.mockMvc.perform(usersRequest).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().is3xxRedirection()).
                andExpect(MockMvcResultMatchers.redirectedUrl("/users/" + user.getId()));
    }
}
