package testcases.superset.model_based_dataset.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import testcases.*;

public class OriginalWebDriver extends ChromeDriver {
    WebDriverWait wait;

    public OriginalWebDriver() {
        super();
        wait = new WebDriverWait(this, 10);
    }

    public OriginalWebDriver(ChromeOptions options) {
        super(options);
        wait = new WebDriverWait(this, 10);
    }

    public OriginalWebDriver login() throws Exception {
        this.get(Constants.getSupersetUrl() + "/login/");
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        this.findElement(By.id("username")).sendKeys("admin");
        this.findElement(By.id("password")).sendKeys("admin");
        this.findElement(By.cssSelector("[type=\"submit\"]")).click();

        return this;
    }
//
//    public OriginalWebDriver visitChartByName(String name) throws Exception {
//        this.get(`$ {
//            BASE_EXPLORE_URL
//        } {
//            "slice_id":$ {
//                response.body.pks[0]
//            }
//        }`);
//        /* unsupported request syntax */
//        ;
//        ;
//
//        return this;
//    }
//
//    public OriginalWebDriver visitChartById(String chartId) throws Exception {
//        this.get(`$ {
//            BASE_EXPLORE_URL
//        } {
//            "slice_id":$ {
//                chartId
//            }
//        }`);
//
//        return this;
//    }
//
//    public OriginalWebDriver visitChartByParams(String params) throws Exception {
//        this/* unsupported method: stringify */;
//        this.get(url);
//
//        return this;
//    }
//
//    public OriginalWebDriver verifySliceContainer(String chartSelector) throws Exception {
//        // Unsupported expect chain: expect.greaterThan
//        // Unsupported expect chain: expect.greaterThan
//        WebElement element2 = this.findElement(By.cssSelector("chartSelector"));
//        AssertJUnit.assertTrue(element2.isDisplayed());
//        element2;
//        ;
//        WebElement element0 = this.findElement(By.cssSelector(".slice_container"));
//        AssertJUnit.assertTrue(element0.isDisplayed());
//        WebElement scopeElement1 = element0;
//        if (chartSelector) {
//        }
//        ;
//        scopeElement1;
//
//        return this;
//    }
//
//    public OriginalWebDriver verifySliceSuccess(String {
//        waitAlias, querySubstring, chartSelector,
//    })throws
//
//    Exception {
//        this.verifySliceContainer(chartSelector);
//        if (querySubstring instanceof RegExp) {
//            // Unsupported expect chain: expect.to.match
//        } else {
//            // Unsupported expect chain: expect.to.contain
//        }
//        this.wait(waitAlias);
//        if (querySubstring) {
//        }
//        ;
//
//        return this;
//    }
}
