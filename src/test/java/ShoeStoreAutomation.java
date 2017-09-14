import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShoeStoreAutomation {

    public static WebDriver driver = new ChromeDriver();
//    public static WebDriverWait webDriverWait = new WebDriverWait(driver,10);

    static final By LNKS_MONTH = By.cssSelector("div#header_nav nav a");
    static final By SHOE_CARDS = By.className("shoe_result");
    static final By SHOE_DESCRIPTION = By.className("shoe_description");
    static final By SHOE_IMAGE = By.cssSelector("td.shoe_image img");
    static final By SHOE_PRICE = By.className("shoe_price");
    static final By INPUT_EMAIL = By.id("remind_email_input");
    static final By BTN_EMAIL_SUBMIT = By.cssSelector("div.left input[type='submit']");
    static final By NOTIFICATION = By.cssSelector("div.flash.notice");

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }

    @Test
    public void shoesShouldHaveDetailsTest(){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://shoestore-manheim.rhcloud.com/");

        //Story 1: Monthly Display of new releases
        //Should display a 'small blurb' of each shoe
        //Should display an image each shoe being released
        //Each should should have a suggested price pricing
        int numberOfMonthLinks = getNumberOfElementsFound(LNKS_MONTH);

        System.out.println("Month amount (should be 12): "+numberOfMonthLinks);
        SoftAssert softAssert = new SoftAssert();
        for(int i = 0; i < numberOfMonthLinks; i++){

            getElementWithIndex(LNKS_MONTH,i).click();

            List<WebElement> shoeResults = driver.findElements(SHOE_CARDS);
            WebElement shoeResult = null;
            String shoeBlurb = null;
            WebElement shoeImage = null;
            String shoePrice = null;
            for(int j = 0; j < shoeResults.size(); j++){
                shoeResult = shoeResults.get(j);
                shoeBlurb = shoeResult.findElement(SHOE_DESCRIPTION).getText();
                shoeImage = shoeResult.findElement(SHOE_IMAGE);
                shoePrice = shoeResult.findElement(SHOE_PRICE).getText();

                softAssert.assertNotEquals("Shoe has a 'blurb'.", "", shoeBlurb);
                //Do we just have to confirm the section exists or do there need to be images? Assuming the latter
                softAssert.assertNotEquals("Shoe has an image", "", shoeImage.getAttribute("src"));
                softAssert.assertNotEquals("Shoe has a price.", "", shoePrice);
            }
        }
        softAssert.assertAll();
    }

    @Test
    public void shouldSubmitEmailTest(){
        //1) There should be an area to submit email address
        //2) on successful submission of a valid email address user should receive a message:
        // "Thanks! We will notify you of our new shoes at this email: users email address"

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://shoestore-manheim.rhcloud.com/");

        //Find the Email, confirm it's displayed and enabled, then input an email address
        WebElement emailInput = driver.findElement(INPUT_EMAIL);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(emailInput.isDisplayed());
        softAssert.assertTrue(emailInput.isEnabled());
        emailInput.sendKeys("test@test.com");

        WebElement submitEmail = driver.findElement(BTN_EMAIL_SUBMIT);
        submitEmail.click();

        WebElement notification = driver.findElement(NOTIFICATION);

        softAssert.assertEquals("Thanks! We will notify you of our new shoes at this email: test@test.com",notification.getText(),
                "Notification message is not 'Thanks! We will notify you of our new shoes at this email: test@test.com");
        softAssert.assertAll();
    }

    private int getNumberOfElementsFound(By by) {
        return driver.findElements(by).size();
    }

    private WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }
}
