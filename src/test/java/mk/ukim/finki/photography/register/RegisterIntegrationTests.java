package mk.ukim.finki.photography.register;

import mk.ukim.finki.photography.config.TestConfiguration;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegisterIntegrationTests {

    MockMvc mockMvc;

    @Autowired
    public UserService userService;

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

    /**
     * This method is being tested using graphs. Edge coverage for source codes or all branches.
     * */

    @Test
    public void testRegisterGet() throws Exception {
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.get("/register");

        mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    // user does not already exist
    @Test
    public void testRegister() throws Exception {
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.post("/register")
                .param("username", "user")
                .param("password", "password")
                .param("repeatedPassword", "password")
                .param("name", "name")
                .param("surname", "surname")
                .param("role", Role.ROLE_USER.toString());

        mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    // Invalid arguments exception
    @Test
    public void testRegisterInvalidArguments() throws Exception {
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.post("/register")
                .param("username", "user")
                .param("password", "password")
                .param("repeatedPassword", "repeatedPassword")
                .param("name", "name")
                .param("surname", "surname")
                .param("role", Role.ROLE_USER.toString());

        mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?error=Passwords are not equal"));
    }

    @Test
    public void testRegisterUsernameTaken() throws Exception {
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("repeatedPassword", "repeatedPassword")
                .param("name", "name")
                .param("surname", "surname")
                .param("role", Role.ROLE_USER.toString());

        mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?error=Passwords are not equal"));
    }
}
