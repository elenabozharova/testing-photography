package mk.ukim.finki.photography.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open()
    {
        driver.get("http://localhost:8080/login");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).isDisplayed();
    }

    public void login(String user, String password) throws InterruptedException {
        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys(user);
        Thread.sleep(5000);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("body > div > div > div.col-sm-4.col-md-4.login > form:nth-child(2) > button")).click();
        Thread.sleep(5000);
    }

    public String getErrorMessage(){
        WebElement errorPage = driver.findElement((By.className("error")));
        return errorPage.getText();
    }


}
