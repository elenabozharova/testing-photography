package mk.ukim.finki.photography.photo;

import mk.ukim.finki.photography.config.TestConfiguration;
import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PhotoIntegrationTests {

    MockMvc mockMvc;

    @Autowired
    public UserService userService;

    @Autowired
    public ImageService imageService;

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
            Image image = new Image("description", "source", "name", user);
            imageService.save(image);
            userService.save(user);
            dataInitialized = true;
        }

    }

    @Test
    @WithMockUser(value = "username", password = "password")
    public void testShowAll() throws Exception {
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.get("/photos");
        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedInUser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("images"))
              //  .andExpect(MockMvcResultMatchers.model().attributeExists("bodyContent","listall"))
                .andExpect(MockMvcResultMatchers.view().name("master"));
    }

    @Test
    @WithMockUser(value = "username", password = "password")
    public void showPhotoTest() throws Exception{
        User user = new User("user123", "password", "name", "surname", Role.ROLE_USER, "", "","");
        Image image = new Image("description", "source", "name", user);
        image = imageService.save(image);

        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.get("/photos/" + image.getId());
        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("loggedInUser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("image"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "image"))
                .andExpect(MockMvcResultMatchers.view().name("master"));
    }

    @Test
    public void testShowAdd() throws Exception
    {
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.get("/photos/upload");
        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "upload"))
                .andExpect(MockMvcResultMatchers.view().name("master"));
    }

    @Test
    public void testAdd() throws Exception
    {
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/photos")
                .param("name", "name")
                .param("source", "source")
                .param("description", "description");

        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/photos"));
    }

    @Test
    @WithMockUser(value = "username", password = "password")
    public void testAddImage() throws Exception
    {
        File image = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\default_picture.jpg");
        FileInputStream fis = new FileInputStream(image);
        MockMultipartFile file = new MockMultipartFile("file", "file", "image/png", fis);
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.multipart("/photos/upload").file(file)
                .param("name", "name")
                .param("description", "description");

        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/photos"));
    }

    @Test
    public void testLike() throws Exception {
        Image image = new Image("description", "source", "name", user);
        image = imageService.save(image);

        User user = userService.findByUsername("username");
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/photos/like/" + image.getId())
                .param("userId", user.getId().toString());
        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/photos"));
    }

    @Test
    public void testUnlike() throws Exception {
        Image image = new Image("description", "source", "name", user);
        image = imageService.save(image);

        User user = userService.findByUsername("username");
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/photos/unlike/" + image.getId())
                .param("userId", user.getId().toString());
        this.mockMvc.perform(userRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/photos"));
    }

    @Test
    public void testEdit() throws Exception {
        Image image = new Image("description", "source", "name", user);
        image = imageService.save(image);
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/photos/edit/" + image.getId())
                .param("name", "name")
                .param("description","description");
        this.mockMvc.perform(userRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void testDelete() throws Exception
    {
        Image image = new Image("description", "source", "name", user);
        image = imageService.save(image);
        MockHttpServletRequestBuilder userRequest = MockMvcRequestBuilders.post("/photos/delete/" + image.getId());
        this.mockMvc.perform(userRequest)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }

}
