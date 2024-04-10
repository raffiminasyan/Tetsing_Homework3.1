import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class Demowebshop {

    private WebDriver driver;
    private List<Float> pricesList = new ArrayList<>();

    @Test
    public void testSeleniumScript() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://demowebshop.tricentis.com/");

        verifyHeaderAndBookCategory();
        addToCartFromBookCategory();
        verifyTopCartContent();
        searchForNonExistingProduct();
        addToCartFromDigitalDownloadsCategory();
        verifyCartTotal();
        cleanup();
    }


    private void verifyHeaderAndBookCategory() throws InterruptedException {
        WebElement header = driver.findElement(By.className("top-menu"));
        assertTrue(header.isDisplayed());

        WebElement bookCategory = driver.findElement(By.xpath("//a[contains(text(),'Book')]"));
        assertTrue(bookCategory.isDisplayed());
    }

    private void addToCartFromBookCategory() throws InterruptedException {
        WebElement bookCategory = driver.findElement(By.xpath("//a[contains(text(),'Book')]"));
        bookCategory.click();
        WebElement addBookButton1 = driver.findElement(By.className("product-box-add-to-cart-button"));
        pricesList.add(Float.parseFloat(driver.findElement(By.className("actual-price")).getText()));

        addBookButton1.click();
        TimeUnit.SECONDS.sleep(2);
    }

    private void verifyTopCartContent() {
        WebElement topCart = driver.findElement(By.id("topcartlink"));
        assertEquals(topCart.findElement(By.className("cart-qty")).getText(), "(1)");
    }

    private void searchForNonExistingProduct() throws InterruptedException {
        WebElement searchInput = driver.findElement(By.id("small-searchterms"));
        WebElement submitButton = driver.findElement(By.className("search-box-button"));

        assertTrue(searchInput.isDisplayed());
        assertTrue(submitButton.isDisplayed());

        searchInput.sendKeys("non-existing-product");
        submitButton.click();
        assertEquals(driver.findElement(By.className("result")).getText(), "No products were found that matched your criteria.");
    }

    private void addToCartFromDigitalDownloadsCategory() throws InterruptedException {
        WebElement digitalCategoryButton = driver.findElement(By.xpath("//a[contains(text(),'Digital downloads')]"));
        digitalCategoryButton.click();

        List<WebElement> productBoxes = driver.findElements(By.className("item-box"));

        for (int i = 0; i < productBoxes.size() - 1; i++) {
            WebElement currentProductBox = productBoxes.get(i);
            WebElement addToCartButton = currentProductBox.findElement(By.className("product-box-add-to-cart-button"));
            addToCartButton.click();
            pricesList.add(Float.parseFloat(currentProductBox.findElement(By.className("actual-price")).getText()));
            TimeUnit.SECONDS.sleep(2);
        }
    }

    private void verifyCartTotal() throws InterruptedException {
        assertEquals(driver.findElement(By.className("cart-qty")).getText(), "(3)");
        driver.findElement(By.id("topcartlink")).click();
        float totalActualPrice = Float.parseFloat(driver.findElement(By.className("cart-total-right")).getText());

        float totalExpectedPrice = 0;
        for (int i = 0; i < pricesList.size(); i++) {
            totalExpectedPrice += pricesList.get(i);
        }

        assertEquals(totalExpectedPrice, totalActualPrice);
    }

    private void cleanup() {
        driver.navigate().to("https://demowebshop.tricentis.com/");
        driver.quit();
    }
}