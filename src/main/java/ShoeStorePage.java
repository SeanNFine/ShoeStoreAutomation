import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

class ShoeStorePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final By LNKS_MONTH = By.cssSelector("div#header_nav nav a");
    private static final By SHOE_CARDS = By.cssSelector("div.shoe_result");
    private static final By SHOE_DESCRIPTION = By.cssSelector("td.shoe_description");
    private static final By SHOE_IMAGE = By.cssSelector("td.shoe_image img");
    private static final By SHOE_PRICE = By.cssSelector("td.shoe_price");
    private static final By INPUT_EMAIL = By.id("remind_email_input");
    private static final By BTN_EMAIL_SUBMIT = By.cssSelector("div.left input[type='submit']");
    private static final By NOTIFICATION = By.cssSelector("div.flash.notice");

    ShoeStorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,10);
        driver.get("http://shoestore-manheim.rhcloud.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(LNKS_MONTH));
    }

    void enterEmail(String s) {
        WebElement emailInput =  driver.findElement(INPUT_EMAIL);
        emailInput.sendKeys(s);
    }

    void clickEmailSubmitButton() {
        WebElement submitEmail = driver.findElement(BTN_EMAIL_SUBMIT);
        submitEmail.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(NOTIFICATION));
    }

    String getNotificationText() {
        WebElement notification = driver.findElement(NOTIFICATION);
        return notification.getText();
    }

    List<WebElement> getShoeResults() {
        return driver.findElements(SHOE_CARDS);
    }

    String getShoeDescriptionFromResult(WebElement shoeResult) {
        return shoeResult.findElement(SHOE_DESCRIPTION).getText();
    }

    /**
     * Checks the Image Src from the given Shoe result and executes a response check against it.
     * Returns true is response is 200 (success)
     * @return boolean if image src response is 200 or not
     */
    boolean isShoeImageFromResultActive(WebElement shoeResult) {
        String imageSrc = shoeResult.findElement(SHOE_IMAGE).getAttribute("src");
        try {
            /*Because an request is being executed for each image, a significant amount of time is being done for each image
                Therefore, the test run very long in order to get an accurate representation of image status*/
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(imageSrc);
            HttpResponse response = client.execute(request);

            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    String getShoePriceFromResult(WebElement shoeResult) {
        return shoeResult.findElement(SHOE_PRICE).getText();
    }

    String getShoeImageSrcFromResult(WebElement shoeResult) {
        return shoeResult.findElement(SHOE_IMAGE).getAttribute("src");
    }

    boolean isEmailInputDisplayedAndEnabled() {
        WebElement emailInput = driver.findElement(INPUT_EMAIL);
        return emailInput.isDisplayed() && emailInput.isEnabled();
    }

    void clickMonthLinkByName(String month) {
         /*Page seems to take a significant time (Approx 3 minutes) to load, particularly due to the images loading.
            Could not find a way around this without breaking the remainder of the tests.
            Therefore the tests run, but take a significant time because of load issues.

          One could implement a pageLoadTimeout and the test would still fail for good reason
            since the images are not loading properly.*/
        driver.findElement(By.linkText(month)).click();
        wait.until(ExpectedConditions.titleContains(month));
    }
}
