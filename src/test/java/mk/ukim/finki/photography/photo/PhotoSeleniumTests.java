package mk.ukim.finki.photography.photo;

import lombok.val;
import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.pages.HomePage;
import mk.ukim.finki.photography.pages.LoginPage;
import mk.ukim.finki.photography.service.AuthService;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.apache.tomcat.jni.Local;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.social.facebook.api.Photo;
import org.springframework.test.context.junit4.SpringRunner;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhotoSeleniumTests {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private WebDriver driver;
    public WebDriverWait wait;

    public PhotoSeleniumTests()
    {

    }

    @BeforeAll
    public void setup() {
        driver = getDriver();
    }


    // test for rendering of login page

    // open home page test

    // TESTS FOR LINKS ON HOMEPAGE

    public void seeOurUserProfiles() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        var seeOurUserProfilesLink = driver.findElement(By.cssSelector("body > section > div.cards > div:nth-child(1) > div > div > a"));
        seeOurUserProfilesLink.click();
        var findPhotographerTitle = driver.findElement(By.xpath("/html/body/section/div/h1"));
        assertEquals("Find a photographer for your photo shoot ideas", findPhotographerTitle.getText());
    }

    @Test
    public void goToRegisterPage() {
        driver = getDriver();
        HomePage homePage = new HomePage(driver);
        homePage.open();
        var registerLink = driver.findElement(By.cssSelector(".cards .row:nth-of-type(2) .card-link"));
        registerLink.click();
        var userNameField = driver.findElement(By.name("username"));
        var passwordField = driver.findElement(By.name("password"));
        var repeatPassword = driver.findElement(By.name("repeatedPassword"));
        var nameField = driver.findElement(By.name("name"));
        var surnField = driver.findElement(By.name("surname"));
        var roleFields = driver.findElements(By.name("role"));
        var linkedinField = driver.findElement(By.name("linkedin"));
        var facebookField = driver.findElement(By.name("facebook"));
        assertNotEquals(null, userNameField);
        assertNotEquals(null, passwordField);
        assertNotEquals(null, repeatPassword);
        assertNotEquals(null, nameField);
        assertNotEquals(null, surnField);
        assertEquals(2, roleFields.size());
        assertNotEquals(null, linkedinField);
        assertNotEquals(null, facebookField);
    }

    // test if the photo has been uploaded
    @Test
    public void uploadPhoto() throws IOException {
        int numberOfPhotos = imageService.list().size();
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
        Assert.assertEquals(imageService.list().size(), numberOfPhotos + 1 );
    }



    private WebDriver getDriver(){
        System.setProperty("webdriver.chrome.driver", "E:\\Projects\\web programming\\photography test\\src\\main\\resources\\chromedriver.exe");
        return new ChromeDriver();
    }

}
