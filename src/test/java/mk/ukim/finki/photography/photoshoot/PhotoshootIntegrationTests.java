package mk.ukim.finki.photography.photoshoot;

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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PhotoshootIntegrationTests {

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
            User user2 = new User("user2", "password", "name", "surname", Role.ROLE_PHOTOGRAPHER, "", "","");
            userService.save(user2);
            dataInitialized = true;
        }

    }

    @Test
    @WithMockUser(username = "username", password = "password", roles = "USER")
    public void testGetACalendar() throws Exception {
        User user = this.userService.findByUsername("username");
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.get("/calendar/" + user.getId());
        this.mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("photographer")).
                andExpect(MockMvcResultMatchers.model().attributeExists("user")).
                andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "bookaphotoshoot")).
                andExpect(MockMvcResultMatchers.view().name("master"));
    }

    @Test
    @WithMockUser(username = "username", password = "password", roles = "USER")
    public void testBook() throws Exception {
        User user2 = userService.findByUsername("user2");
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.post("/calendar/book/" + user2.getId())
                .param("date", LocalDate.now().toString())
                .param("time", LocalTime.now().toString())
                .param("partOfDay", "morning");

        this.mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/" + user2.getId()));

    }

    @Test
    public void getBookedForPhotographer() throws Exception {
        User user2 = userService.findByUsername("user2");
        MockHttpServletRequestBuilder usersRequest = MockMvcRequestBuilders.get("/calendar/booked/" + user2.getId());
        this.mockMvc.perform(usersRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("list"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "bookedphotoshoots"))
                .andExpect(MockMvcResultMatchers.view().name("master"));
    }
}
