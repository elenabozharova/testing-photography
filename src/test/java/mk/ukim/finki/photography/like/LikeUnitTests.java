package mk.ukim.finki.photography.like;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.ImageRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.ImageServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class LikeUnitTests {

    /**
     * Tests for the like methods in the ImageService
     * */

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    private ImageServiceImpl service;

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);

        File uploadedImage1 = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\camera.png");
        FileInputStream is1 = new FileInputStream(uploadedImage1);
        User user = new User("photographer", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        user.setId((long)12345);

        // this user returns null when called findById
        User user2 = new User("user", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        user2.setId((long)7896);

        // this user will be found by Id
        User testUser = new User("username", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        testUser.setId((long)4789);

        Image image = new Image("description", Base64.getEncoder().encodeToString(is1.readAllBytes()), "imgName", user, LocalDate.now());

        Mockito.when(imageRepository.findById((long) 1234)).thenReturn(null);
        Mockito.when(imageRepository.findById((long) 4569)).thenReturn(Optional.of(image));
        Mockito.when(imageRepository.findById(null)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById((long) 7896)).thenReturn(null);
        Mockito.when(userRepository.findById((long)4789)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.findById(null)).thenReturn(Optional.empty());
        service = Mockito.spy(new ImageServiceImpl(imageRepository, userRepository));
    }

    /**
     * Tests for the like method with ISP interface based approach.
     * Characteristics:
     *       A - image id is null
     *          b1 - true
     *          b2 - false
     *       B - user id in null
     *          b1 - true
     *          b2 - false
     *
     *  Test criterion:
     *        Base choice coverage:
     *          A2 B2
     *          A1 B2
     *          A2 B1
     * */

    @Test
    public void testLikeImageT1(){
        // image id is not null and user id is not null
        Optional<User> user = userRepository.findById((long) 4789);
        Optional<Image> image = this.imageRepository.findById((long) 4569);
        this.service.likeImage((long) 4569, user.get().getId());
        Assert.assertEquals(1, user.get().getLikes().size());
    }

    @Test
    public void testLikeImageT2() {
        Assert.assertThrows("NoSuchElementException expected", NoSuchElementException.class, () -> this.service.likeImage(null, (long) 4569));
    }

    @Test
    public void testLikeImageT3() {
        Assert.assertThrows("NoSuchElementExceptionExpected", NoSuchElementException.class, () -> this.service.likeImage((long)1234, null));
    }


    @Test
    public void testLikeImageNotFound()
    {
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, () ->  this.service.likeImage((long)1234,(long)4789));
    }

    @Test
    public void testLikeImageUserNotFound()
    {
        Optional<User> user = this.userRepository.findById((long)7896);
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, () -> this.service.likeImage((long) 4569, user.get().getId()));
    }

    @Test
    public void testLikeImage()
    {
        Optional<User> user = userRepository.findById((long) 4789);
        Optional<Image> image = this.imageRepository.findById((long) 4569);
        this.service.likeImage((long) 4569, user.get().getId());
        Assert.assertEquals(1, user.get().getLikes().size());
    }

    @Test
    public void testUnlikeImageNotFound()
    {
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, () ->  this.service.unlikeImage((long)1234, (long)4789));
    }

    @Test
    public void testUnlikeUserNotFound()
    {
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, () ->  this.service.unlikeImage((long) 4569, (long)7896));
    }

    @Test
    public void testUnlikeImage()
    {
        Optional<User> user = this.userRepository.findById((long)4789);
        this.service.likeImage((long) 4569, user.get().getId());
        Assert.assertEquals(1, user.get().getLikes().size());
        this.service.unlikeImage((long)4569, (long)4789);
        Assert.assertEquals(0, user.get().getLikes().size());
    }
}
