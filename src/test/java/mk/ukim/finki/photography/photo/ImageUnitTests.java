package mk.ukim.finki.photography.photo;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.UsernameNotFoundException;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.aspectj.bridge.MessageUtil.fail;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ImageUnitTests {
/**
 * A test for the ImageService implementation class.
 * The results from the repositories are not tested here.
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

        Image image = new Image("description", Base64.getEncoder().encodeToString(is1.readAllBytes()), "imgName", user, LocalDate.now());

        File uploadedImage2 = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\photography.jpg");
        FileInputStream is2 = new FileInputStream(uploadedImage2);
        Image image2 = new Image("description", Base64.getEncoder().encodeToString(is2.readAllBytes()), "name", user, LocalDate.now());

        List<Image> images = new ArrayList<>();
        images.add(image);
        images.add(image2);

        Mockito.when(imageRepository.findById((long)1687)).thenReturn(null);
        Mockito.when(imageRepository.findById((long)1234)).thenReturn(Optional.of(image));
        Mockito.when(imageRepository.findById((long)-145)).thenReturn(Optional.of(image));
        Mockito.when(imageRepository.findById((long)0)).thenReturn(Optional.of(image2));
        Mockito.when(imageRepository.findAll()).thenReturn(images);
        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(image);
        Mockito.when(imageRepository.save(null)).thenReturn(null);
        Mockito.when(imageRepository.findAllByUserId((long)12345)).thenReturn(images);
        service = Mockito.spy(new ImageServiceImpl(imageRepository, userRepository));
    }

    /**
     * Tests for the save  public Image save(Image image) method using ISP interface method.
     * Characteristics:
     *          C1 - Image is null
     *               b1 - true
     *               b2 - false
     * */

    @Test
    public void testSaveT1() {
        Image image = service.save(null);
        Assert.assertNull(image);
    }

    @Test
    public void testSaveT2() throws IOException {
        File uploadedImage = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\camera.png");
        FileInputStream is = new FileInputStream(uploadedImage);
        User user = new User("photographer", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        Image image = new Image("description", Base64.getEncoder().encodeToString(is.readAllBytes()), "imgName", user, LocalDate.now());

        Image result = service.save(image);

        Assert.assertEquals(image.getDescription(), result.getDescription());
        Assert.assertEquals(image.getImageSrc(), result.getImageSrc());
        Assert.assertEquals(image.getImgName(), result.getImgName());
        Assert.assertEquals(image.getUser().getUsername(), result.getUser().getUsername());
        Assert.assertEquals(image.getDate(), result.getDate());
    }

    /**
     * Additional tests for the save method when user is not found using graph edge coverage.
     * */

    @Test
    public void testSaveImageToDbUserNotFound() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser("test"); // this user does not exist in the test database
        File image = new File("E:\\Projects\\web programming\\photography test\\src\\main\\resources\\static\\img\\camera.png");
        FileInputStream fis = new FileInputStream(image);
        MockMultipartFile file = new MockMultipartFile("file", fis );

        String imageName = "name";
        String description = "description";
        LocalDate time = LocalDate.now();

        Assert.assertThrows("UsernameNotFoundException expected", UsernameNotFoundException.class, () -> service.saveImagetoDB(request, file, imageName, description));
    }

    /**
     * Tests for the list method.
     * */

    @Test
    public void testList()
    {
        List<Image> result = service.list();
        Assert.assertEquals(2, result.size());
    }

    /**
     * Tests for the findAllByUser function.
     * */

    @Test
    public void testFindAllByUser()
    {
        List<Image> result = service.findAllByUser((long)12345);
        Assert.assertEquals(2, result.size());
    }

    /**
     * Tests for the findById method. The method id ISP interface approach.
     * Characteristics:
     *      C1 - value of Id
     *          b1 - Id < 0
     *          b2 - Id = 0
     *          b3 - Id > 0
     * */

    @Test
    public void testFindByIdT1() {
        Assert.assertThrows("NoSuchElement exception expected", NoSuchElementException.class, ()->  this.service.findById((long)-145));
    }

    @Test
    public void testFindByIdT2() {
        Assert.assertThrows("NoSuchElement exception expected", NoSuchElementException.class, ()->  this.service.findById((long)0));
    }


    @Test
    public void testFindByIdT3()
    {
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, ()->         this.service.findById((long)1687));
    }

    @Test
    public void testEditImageNotFound()
    {
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, () -> this.service.editImage((long)1687, "description", "name"));
    }

    /**
     * Tests for the editImage function , with parameters Id, description, name, using ISP interface approach.
     * Characteristics:
     *      A - value of id
     *          b1 - Id < 0
     *          b2 - Id = 0
     *          b3 - Id > 0
     *      B - description is null
     *          b1 - true
     *          b2 - false
     *      C - name is null
     *          b1 - true
     *          b2 - false
     *
     *  Test criterion:
     *     Base choice coverage.
     *     Base choice -> A3 B2 C2 - id > 0, description not null, name not null
     *     Test cases:    A3 B1 C2 - id > 0, description null, name not null
     *                    A3 B1 C1 - id > 0 , description null, name null
     *                    A1 B2 C2 - id < 0, description not null, name not null
     *                    A2 B2 C2 - id = 0, description not null, name not null
     * */

    @Test
    public void testEditImageT1()
    {
        try{
            service.editImage((long)1234, "description", "name");
        }
        catch (Exception e)
        {
            fail("An exception was thrown");
        }
    }

    @Test
    public void testEditImageT2(){
        // the save functionality is mocked so the repository will return an image with a description ! in a real use, it will return null for description
        Image image =  service.editImage((long)1234, null, "name");
        Assert.assertNotNull(image);
    }

    @Test
    public void testEditImageT3() {
        // the save functionality is mocked so the repository will return an image with a description and name ! in a real use, it will return null !
        Image image =  service.editImage((long)1234, null, null);
        Assert.assertNotNull(image);
    }

    @Test
    public void testEditImageT4(){
        Image image = service.editImage((long)-145, "description", "name");
        Assert.assertNotNull(image);
    }

    @Test
    public void testEditImageT5(){
        Image image = service.editImage((long)0, "description", "name");
        Assert.assertNotNull(image);
    }

}
