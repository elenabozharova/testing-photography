package mk.ukim.finki.photography.login;

import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.pages.LoginPage;
import mk.ukim.finki.photography.service.AuthService;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginSeleniumTests {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private WebDriver driver;
    public WebDriverWait wait;

    @Test
    public void openLoginPage() throws InterruptedException {
        driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        assertTrue(loginPage.isLoaded());
    }

    // test bad credentials login

    @Test
    public void loginBadCredentials(){
        driver = getDriver();
        User user = new User();
        user.setUsername("sam.jasmine@gmail.com");
        user.setPassword("test");
        if(userService.findByUsername("sam.jasmine@gmail.com") == null)
            userService.save(user);

        // navigate to login page
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // try logging in with wrong password
        var username = driver.findElement(By.name("username"));
        var password = driver.findElement(By.name("password"));

        username.sendKeys("sam.jasmine@gmail.com");
        password.sendKeys("test");

        // click log in
        driver.findElement(By.cssSelector("form[action='/login'] button")).click();

        var errorMessage = driver.findElement(By.cssSelector(".error"));
        assertEquals("BadCredentials", errorMessage.getText() );

    }

    // test login
    @Test
    public void login() throws InterruptedException {
        driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("user@test.com", "test");
        assertEquals("http://localhost:8080/home", driver.getCurrentUrl());
    }

    // test redirect to facebook login page

    @Test
    public void loginFacebook() {
        driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        driver.findElement(By.cssSelector("body > div > div > div.col-sm-4.col-md-4.login > form:nth-child(3) > input[type=submit]:nth-child(2)")).click();
        var loginMessage = driver.findElement(By.className("_9axz"));
        assertEquals( "Најави се на Facebook", loginMessage.getText());

    }


    private WebDriver getDriver(){
        System.setProperty("webdriver.chrome.driver", "E:\\Projects\\web programming\\photography test\\src\\main\\resources\\chromedriver.exe");
        return new ChromeDriver();
    }
}
