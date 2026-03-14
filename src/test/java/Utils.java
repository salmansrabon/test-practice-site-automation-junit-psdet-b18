import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Utils {
    public static void scrollDown(WebDriver driver){
        JavascriptExecutor js=(JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }
    public static int generateRandomNumber(int min, int max){
        double randomNumber= Math.random()*(max-min)+min;
        return (int)randomNumber;
    }
    public static void explicitWait(WebDriver driver, WebElement webElement, int timeout){
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(timeout));
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.tagName("h2"))));
        wait.until(d->webElement);
    }
}
