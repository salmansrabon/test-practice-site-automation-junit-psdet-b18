import com.github.javafaker.Faker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunit {
    WebDriver driver;
    @BeforeAll
    public void setup() throws IOException, ParseException {
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get("http://localhost:3000");
        Cookies cookies=new Cookies(driver);
        cookies.setCookie();
    }
    @Test
    public void login() throws InterruptedException, IOException {
        driver.get("http://localhost:3000");
//        driver.findElement(By.id("email")).sendKeys("admin@test.com");
//        driver.findElement(By.id("password")).sendKeys("1234");
        List<WebElement> element= driver.findElements(By.className("form-control"));
        element.get(0).sendKeys("admin@test.com"); //write email
        element.get(1).sendKeys("1234"); //write password
        driver.findElement(By.cssSelector("[type=submit]")).click();
        //Thread.sleep(1000);
        //String headerTextActual= driver.findElement(By.tagName("h2")).getText();
        String headerTextActual= driver.findElement(By.xpath("//h2[contains(text(),\"Profile\")]")).getText();
        String headerTextExpected="Profile";
        Assertions.assertEquals(headerTextExpected,headerTextActual);

        String currentUrlActual= driver.getCurrentUrl();
        Assertions.assertTrue(currentUrlActual.contains("/dashboard/profile"));
        Cookies cookies=new Cookies(driver);
        cookies.getCookie();
    }
    @Test
    public void buttonClickHandling() throws InterruptedException {
        driver.get("http://localhost:3000/dashboard/practice-components");
        Utils.scrollDown(driver);
        Actions actions=new Actions(driver);
        actions.doubleClick(driver.findElement(By.id("doubleClickBtn"))).perform();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
        actions.contextClick(driver.findElement(By.id("rightClickBtn"))).perform();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

    }
    @Test
    public void nextJSDateTimeHandling(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement dateTimeElem= driver.findElement(By.cssSelector("[type=datetime-local]"));
        dateTimeElem.click();
        dateTimeElem.sendKeys("10","Feb");
        dateTimeElem.sendKeys(Keys.ARROW_RIGHT,"2025");
    }
    @Test
    public void readOnlyDateHandling(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement dateElem= driver.findElements(By.className("form-control")).get(1);
        JavascriptExecutor js=(JavascriptExecutor) driver;
        //js.executeScript("arguments[0].removeAttribute('readonly')", dateElem);
        js.executeScript("arguments[0].value='11/24/2025'", dateElem);
    }
    @Test
    public void editableDateHandling(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement dateElem= driver.findElement(By.cssSelector("[placeholder=\"Select date\"]"));
        dateElem.sendKeys(Keys.CONTROL+"a",Keys.BACK_SPACE);
        dateElem.sendKeys("01/01/2026",Keys.ENTER);
    }
    @Test
    public void handleNewTab(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        driver.findElement(By.id("newTabBtn")).click();
        ArrayList<String> windows=new ArrayList(driver.getWindowHandles());
        System.out.println(windows);
        driver.switchTo().window(windows.get(1));
        String headerActual= driver.findElement(By.tagName("h1")).getText();
        String headerExpected="Example Domain";
        Assertions.assertEquals(headerExpected,headerActual);
        driver.close();
        driver.switchTo().window(windows.get(0));

    }
    @Test
    public void handleNewWindow(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        driver.findElement(By.id("newWindowBtn")).click();
        String mainWindow= driver.getWindowHandle();
        Set<String> allWindows= driver.getWindowHandles();
        for(String window:allWindows){
            if(!window.equals(mainWindow)){
                driver.switchTo().window(window);
                String headerName= driver.findElement(By.tagName("h1")).getText();
                System.out.println(headerName);
                driver.close();
                break;
            }
        }
        driver.switchTo().window(mainWindow);

    }
    @Test
    public void getUsersData() throws InterruptedException {
        driver.get("http://localhost:3000/dashboard/users");
        //Thread.sleep(2000);
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(40));
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.tagName("h2"))));
        wait.until(d->driver.findElement(By.tagName("h2")));

        WebElement table= driver.findElement(By.tagName("tbody"));
        List<WebElement> allRows= table.findElements(By.tagName("tr"));
        for(WebElement row:allRows){
            List<WebElement> cells= row.findElements(By.tagName("td"));
            for(WebElement cell:cells){
                System.out.println(cell.getText());
            }
        }
    }
    @Test
    public void handleIFrame(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement frame= driver.findElement(By.tagName("iframe"));
        driver.switchTo().frame(frame);
        String header= driver.findElement(By.tagName("h1")).getText();
        System.out.println(header);
        driver.switchTo().defaultContent();
    }
    @Test
    public void createNewUser() throws InterruptedException, IOException, ParseException {
        driver.get("http://localhost:3000/dashboard/add-user");
        driver.findElement(By.id("photoInput")).sendKeys(System.getProperty("user.dir")+"/src/test/resources/logo.png");
        List<WebElement> userForm= driver.findElements(By.className("form-control"));
        Faker faker=new Faker();
        userForm.get(0).sendKeys(faker.name().firstName()); //firstname
        userForm.get(1).sendKeys(faker.name().lastName()); //lastname
        String email=faker.name().firstName().toLowerCase()+"@test.com";
        userForm.get(2).sendKeys(email); //email
        String phoneNumber="0130"+Utils.generateRandomNumber(1000000,9999999);
        userForm.get(3).sendKeys(phoneNumber); //phone number

        List<WebElement> dropdownElem= driver.findElements(By.className("form-select"));
        dropdownElem.get(0).click(); //dynamic dropdown
        for(int i=0;i<2;i++){
            dropdownElem.get(0).sendKeys(Keys.ARROW_DOWN);
            Thread.sleep(500);
        }
        Utils.scrollDown(driver);
        dropdownElem.get(0).sendKeys(Keys.ENTER);
        String password="1234";
        userForm.get(4).sendKeys(password);

        Select districtDropdown= new Select(dropdownElem.get(1)); //classic dropdown
        districtDropdown.selectByVisibleText("Dhaka");

        driver.findElement(By.id("genderMale")).click();
        driver.findElement(By.id("agreement")).click();
        driver.findElement(By.cssSelector("[type=submit]")).click();

        JSONManipulation.saveUsersData(email,password);

    }
    @Test
    public void updateUser() throws IOException, ParseException {
        driver.get("http://localhost:3000");
        JSONObject userData= JSONManipulation.readJSONData();
        String email= (String) userData.get("email");
        String password= (String) userData.get("password");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("[type=submit]")).click();

        driver.findElement(By.name("Edit")).click();
        WebElement firstNameElem=driver.findElements(By.className("form-control")).get(0);
        firstNameElem.sendKeys(Keys.CONTROL+"a",Keys.BACK_SPACE);
        firstNameElem.sendKeys("Test First Name");
        driver.findElement(By.className("btn-success")).click();

        WebElement successTextElem=driver.findElement(By.className("alert-success"));
        Utils.explicitWait(driver, successTextElem ,40 );
        String successTextActual= successTextElem.getText();
        String successTextExpected="updated successfully";

        Assertions.assertTrue(successTextActual.contains(successTextExpected),"Expectation do not matched");

    }
    @Test
    public void hoverMenu(){
        driver.get("https://www.aiub.edu/");
        Actions actions=new Actions(driver);
        //List<WebElement> element=driver.findElements(By.xpath("//a[contains(text(),\"About\")]"));
        List<WebElement> element= driver.findElements(By.partialLinkText("ABOUT"));
        actions.moveToElement(element.get(0)).perform();

        driver.findElement(By.partialLinkText("ABOUT")).click();
    }
    //@AfterAll
    public void closeDriver(){
        driver.quit();
    }
}
