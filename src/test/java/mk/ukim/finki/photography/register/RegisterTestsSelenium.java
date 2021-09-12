package mk.ukim.finki.photography.register;

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

@SpringBootTest
@RunWith(SpringRunner.class)
public class RegisterTestsSelenium {
    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private WebDriver driver;
    public WebDriverWait wait;
    // test register
    @Test
    public void register()
    {
        driver = getDriver();
        driver.get("http://localhost:8080/register");

        var userNameField = driver.findElement(By.name("username"));
        var passwordField = driver.findElement(By.name("password"));
        var repeatPassword = driver.findElement(By.name("repeatedPassword"));
        var nameField = driver.findElement(By.name("name"));
        var surnameField = driver.findElement(By.name("surname"));

        if(userService.findByUsername("user5@test.com") == null)
            userNameField.sendKeys("user5@test.com");
        else
            userNameField.sendKeys("user" + 7+ "@test.com");

        passwordField.sendKeys("test");
        repeatPassword.sendKeys("test");
        nameField.sendKeys("Steven");
        surnameField.sendKeys("Cornel");

        var roleField = driver.findElement(By.cssSelector("#ROLE_USER"));
        roleField.click();

        driver.findElement(By.cssSelector("form[action='/register'] button")).click();

        // check if the user is redirected to login page
        var login = driver.findElement(By.cssSelector(".login h2")).getText();
        assertEquals("Log in", login);
    }



    private WebDriver getDriver(){
        System.setProperty("webdriver.chrome.driver", "E:\\Projects\\web programming\\photography test\\src\\main\\resources\\chromedriver.exe");
        return new ChromeDriver();
    }
}
