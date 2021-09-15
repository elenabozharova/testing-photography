package mk.ukim.finki.photography.follow;

import mk.ukim.finki.photography.config.TestConfiguration;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.FollowService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FollowIntegrationTests {

    MockMvc mockMvc;

    @Autowired
    public UserService userService;

    @Autowired
    public FollowService followService;

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
            User user2 = new User("userTest", "password", "name", "surname", Role.ROLE_USER, "", "","");

            userService.save(user);
            userService.save(user2);
            dataInitialized = true;
        }
    }

    @Test
    @WithMockUser(value = "username", password = "password")
    public void followUser() throws Exception {
        User user = userService.findByUsername("userTest");
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.post("/follow/" + user.getId() );
        this.mockMvc.perform(usersRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/" + user.getId()));
    }

    @Test
    @WithMockUser(value = "username", password = "password")
    public void unfollowUser() throws Exception {
        User user1 = userService.findByUsername("username");
        User user2 = userService.findByUsername("userTest");
        followService.follow(user1.getId(), user2.getId());
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/follow/unflw/" + user2.getId());
        this.mockMvc.perform(userRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/" + user2.getId()));
    }

}
