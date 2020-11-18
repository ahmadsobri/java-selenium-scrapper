package utility;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class WebScrapper {

    private static WebDriver driver = new FirefoxDriver();

    public static List<String> getProductLinks(int total) {
        List<String> links = new ArrayList<>();
        int pageNumber = 0;
        String xPathListElement = "//*[@data-testid=\"lstCL2ProductList\"]";
        String xPathLinkProduct = xPathListElement + "//a[@data-testid=\"lnkProductContainer\"]";

        while (links.size() < total) {
            try {
                pageNumber = pageNumber + 1;
                String url = "https://www.tokopedia.com/p/handphone-tablet/handphone?page=" + pageNumber + "&rt=4,9"; // with rate filter (out of 5 stars)

                waitScrollDown(url,xPathListElement,5);
                List<WebElement> linkProductList = driver.findElements(By.xpath(xPathLinkProduct));

                for (WebElement webElement : linkProductList) {
                    if (links.size() < total) {
                        String linkDetail = webElement.getAttribute("href");
                        links.add(linkDetail);
                    }
                }
            } catch (Exception ex) {
                break;
            }
        }
        return links;
    }

    public static ProductModel getDetailProduct(String link) {
        ProductModel productModel = new ProductModel();
        String xPathElement = "//*[@data-testid=\"pdpContainer\"]";
        String xPathRate = xPathElement + "//span[@data-testid=\"lblPDPDetailProductRatingNumber\"]";
        String xPathProductName = xPathElement + "//h1[@data-testid=\"lblPDPDetailProductName\"]";
        String xPathPrice = xPathElement + "//h3[@data-testid=\"lblPDPDetailProductPrice\"]";
        String xPathDescTitle = xPathElement + "//span[@data-testid=\"lblPDPDeskripsiProdukTitle\"]";
        String xPathDesc = xPathElement + "//p[@data-testid=\"lblPDPDeskripsiProduk\"]";
        String xPathImg = xPathElement + "//div[@data-testid=\"PDPImageMain\"]";

        waitScrollDown(link,xPathElement,10);

        WebElement rateElement = driver.findElement(By.xpath(xPathRate));
        WebElement productNameElement = driver.findElement(By.xpath(xPathProductName));
        WebElement proceElement = driver.findElement(By.xpath(xPathPrice));
        WebElement titleElement = driver.findElement(By.xpath(xPathDescTitle));
        WebElement descElement = driver.findElement(By.xpath(xPathDesc));
        WebElement imgDivElement = driver.findElement(By.xpath(xPathImg));
        WebElement imgElement = imgDivElement.findElement(By.tagName("img"));

        String name = productNameElement.getText();
        String price = proceElement.getText();
        String rate = rateElement.getText();
        String description = titleElement.getText() + "\n" + descElement.getText();
        String imgLink = imgElement.getAttribute("src");

        productModel.setNameOfProduct(name);
        productModel.setPrice(price);
        productModel.setRating(rate);
        productModel.setDescription(description);
        productModel.setImageLink(imgLink);

        return productModel;
    }

    private static void scrollHight(JavascriptExecutor js, long hight, long timeOutInSecond) {
        hight = hight == 0 ? 1 : hight;
        long time_sleep = timeOutInSecond;
        long check_height = (long) js.executeScript("return document.body.scrollHeight;");
        long i = 10;
        while (check_height > i) {
            js.executeScript("window.scrollTo(0, " + i + ");");
            try {
                TimeUnit.SECONDS.sleep(time_sleep);
                check_height = (long) js.executeScript("return document.body.scrollHeight;");
                i += (check_height / hight);
            } catch (Exception ex) {
                break;
            }
        }
    }

    private static void waitScrollDown(String url, String xPath, long timeOutInSecond){
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSecond);
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        scrollHight(js,8, 2);
    }
}
