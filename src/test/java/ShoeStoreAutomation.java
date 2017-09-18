import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class ShoeStoreAutomation {

    private static WebDriver driver = new ChromeDriver();

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }

    /**
     * Test to cover Story 1: Monthly Display of new releases
     * 1) Shoe Entries should display a 'small blurb' of each shoe
     * 2) Shoe entries should display an image each shoe being released
     * 3) Each shoe should have a suggested price pricing
     */
    @Test
    public void shoesShouldHaveDetailsTest(){
        ShoeStorePage shoeStorePage = new ShoeStorePage(driver);

        int numberOfMonthLinks = shoeStorePage.getNumberOfMonthLinks();

        System.out.println("Month amount (should be 12): "+numberOfMonthLinks);
        SoftAssert softAssert = new SoftAssert();
        for(int i = 0; i < numberOfMonthLinks; i++) {
            shoeStorePage.clickMonthLinkByIndex(i);

            List<WebElement> shoeResults = shoeStorePage.getShoeResults();
            WebElement shoeResult;
            String shoeDescription;
            String shoeImageSrc;
            boolean isShoeImageActive;
            String shoePrice;
            for (int j = 0; j < shoeResults.size(); j++) {
                shoeResult = shoeResults.get(j);
                shoeDescription = shoeStorePage.getShoeDescriptionFromResult(shoeResult);
                shoeImageSrc = shoeStorePage.getShoeImageSrcFromResult(shoeResult);
                isShoeImageActive = shoeStorePage.isShoeImageFromResultActive(shoeResult);
                shoePrice = shoeStorePage.getShoePriceFromResult(shoeResult);

                System.out.println("Shoe Desc: " + shoeDescription);
                System.out.println("Shoe Image Src: " + shoeImageSrc);
                System.out.println("Shoe Image Active: " + isShoeImageActive);
                System.out.println("Shoe Price: " + shoePrice);

                softAssert.assertNotEquals(shoeDescription, "", "Shoe does not have a description.");
                softAssert.assertNotEquals(shoeImageSrc, "", "Shoe does not have a image src.");
                softAssert.assertTrue(isShoeImageActive, "Shoe image is not active.");
                softAssert.assertNotEquals(shoePrice, "", "Shoe does not have a price.");
            }
        }
        softAssert.assertAll();
    }

    /**
     * Test to cover Story 2:
     * 1) There should be an area to submit email address
     * 2) on successful submission of a valid email address user should receive a message:
     * "Thanks! We will notify you of our new shoes at this email: users email address"
     */
    @Test
    public void shouldSubmitEmailTest(){
        ShoeStorePage shoeStorePage = new ShoeStorePage(driver);

        shoeStorePage.enterEmail("test@test.com");
        shoeStorePage.clickEmailSubmitButton();
        String notificationText = shoeStorePage.getNotificationText();

        Assert.assertEquals(notificationText,"Thanks! We will notify you of our new shoes at this email: test@test.com",
                "Notification message is not 'Thanks! We will notify you of our new shoes at this email: test@test.com");
    }
}
