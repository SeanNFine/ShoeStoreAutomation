import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class ShoeStoreAutomation {

    private static WebDriver driver;

    /**
     * Test to cover Story 1: Monthly Display of new releases
     * 1) Shoe Entries should display a 'small blurb' of each shoe
     * 2) Shoe entries should display an image each shoe being released
     * 3) Each shoe should have a suggested price pricing
     */
    @Test(dataProvider = "months")
    public void shoesShouldHaveDetailsTest(String month){
        driver = new ChromeDriver();
        ShoeStorePage shoeStorePage = new ShoeStorePage(driver);

        SoftAssert softAssert = new SoftAssert();
        shoeStorePage.clickMonthLinkByName(month);

        List<WebElement> shoeResults = shoeStorePage.getShoeResults();
        WebElement shoeResult;
        for (int j = 0; j < shoeResults.size(); j++) {
            shoeResult = shoeResults.get(j);

            softAssert.assertNotEquals(shoeStorePage.getShoeDescriptionFromResult(shoeResult), "",
                    "Shoe does not have a description.");
            softAssert.assertNotEquals(shoeStorePage.getShoeImageSrcFromResult(shoeResult), "",
                    "Shoe does not have a image src.");
            softAssert.assertTrue(shoeStorePage.isShoeImageFromResultActive(shoeResult),
                    "Shoe image is not active.");
            softAssert.assertNotEquals(shoeStorePage.getShoePriceFromResult(shoeResult), "",
                    "Shoe does not have a price.");
        }

        softAssert.assertAll();
    }

    /**
     * Test to cover Story 2: Submit email for reminder
     * 1) There should be an area to submit email address
     * 2) on successful submission of a valid email address user should receive a message:
     * "Thanks! We will notify you of our new shoes at this email: users email address"
     */
    @Test
    public void shouldSubmitEmailTest(){
        driver = new ChromeDriver();
        ShoeStorePage shoeStorePage = new ShoeStorePage(driver);

        Assert.assertTrue(shoeStorePage.isEmailInputDisplayedAndEnabled(), "Email is not Displayed or not Enabled");

        shoeStorePage.enterEmail("test@test.com");
        shoeStorePage.clickEmailSubmitButton();
        String notificationText = shoeStorePage.getNotificationText();

        Assert.assertEquals(notificationText,"Thanks! We will notify you of our new shoes at this email: test@test.com",
                "Notification message is not 'Thanks! We will notify you of our new shoes at this email: test@test.com");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
        driver.quit();
    }

    /*DataProvider for Months.*/
    @DataProvider
    public static Object[][] months() {
        return new Object[][]{
            {"January"},
            {"February"},
            {"March"},
            {"April"},
            {"May"},
            {"June"},
            {"July"},
            {"August"},
            {"September"},
            {"October"},
            {"November"},
            {"December"}
        };
    }
}
