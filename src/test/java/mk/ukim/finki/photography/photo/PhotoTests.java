package mk.ukim.finki.photography.photo;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhotoTests {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Test
    public void uploadAPhoto() throws IOException {
        Image image1 = new Image();
        image1.setImageSrc("");
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.png");
        final MockMultipartFile photoForUpload = new MockMultipartFile("file", "test.png", "image/png", inputStream);
        image1.setImageSrc(photoForUpload.toString());
        image1.setImgName("image3");
        image1.setDescription("description3");
        image1.setDate(LocalDate.of(2021, 8, 24));
        User user = userService.findByUsername("elena.bozarova");
        image1.setUser(user);
        Image image2 = imageService.save(image1);


    }

}
