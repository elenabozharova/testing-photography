package mk.ukim.finki.photography.login;

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
import org.springframework.security.test.context.support.WithMockUser;
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
public class LoginIntegrationTests {

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
    public void testLogout() throws Exception {
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.get("/logout");
        this.mockMvc.perform(userRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(value = "username", password = "password")
    public void testLogin() throws Exception
    {
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/login")
                .param("username", "username")
                .param("password", "password");
        this.mockMvc.perform(userRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }



}
