package testcases.superset.model_based_dataset.test;

import config.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.*;
import org.testng.annotations.*;
import testcases.*;

import java.util.*;
import java.util.concurrent.*;

public class NativeFiltersTest {
    OriginalWebDriver driver;
    WebDriverWait wait;
    Actions act;
    long milliseconds = new Date().getTime();
    String dashboard = "Test Dashboard" + milliseconds;

    @BeforeClass
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", DriverConfig.DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        driver = new OriginalWebDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(Constants.getSupersetUrl());

        wait = new WebDriverWait(driver, 10);
        act = new Actions(driver);

        driver.login();

        driver.get(Constants.getSupersetUrl() + TestConstant.DASHBOARD_LIST);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test=\"new-dropdown\"]")));
        WebElement dropDown = driver.findElement(By.cssSelector("[data-test=\"new-dropdown\"]"));
        act.moveToElement(dropDown).perform();
//        driver.findElement(By.cssSelector("[data-test=\"menu-item-Dashboard\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/dashboard/new']"))).click();
        driver.findElement(By.cssSelector("[data-test=\"editable-title-input\"]")).sendKeys(dashboard + Keys.ENTER);
        driver.findElement(By.cssSelector("[data-test=\"header-save-button\"]")).click();
        driver.get(Constants.getSupersetUrl() + TestConstant.CHART_LIST);
        driver.findElement(By.cssSelector("[data-test=\"search-input\"]")).sendKeys("Treemap" + Keys.ENTER);
        WebElement element0 = driver.findElement(By.cssSelector("[data-test=\"Treemap-list-chart-title\"]"));
        AssertJUnit.assertTrue(element0.isDisplayed());
        element0.click();
        driver.findElement(By.cssSelector("[data-test=\"query-save-button\"]")).click();
        driver.findElement(By.cssSelector("[data-test=\"save-chart-modal-select-dashboard-form\"]"))
              .findElement(By.cssSelector("#dashboard-creatable-select"))
              .sendKeys(dashboard + Keys.ENTER + Keys.ENTER);
        driver.findElement(By.cssSelector("[data-test=\"btn-modal-save\"]")).click();
    }

    @BeforeMethod
    public void before() throws Exception {
        System.setProperty("webdriver.chrome.driver", DriverConfig.DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        driver = new OriginalWebDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(Constants.getSupersetUrl());

        wait = new WebDriverWait(driver, 10);
        act = new Actions(driver);

        driver.login();

        driver.get(Constants.getSupersetUrl() + TestConstant.DASHBOARD_LIST);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test=\"search-input\"]")));
        driver.findElement(By.cssSelector("[data-test=\"search-input\"]")).sendKeys(dashboard + Keys.ENTER);
        driver.findElement(By.xpath("//*[contains(text(), '[data-test=\"cell-text\"]')]")).click();
    }

    @Test(priority = 0)
    void shouldShowFilterBarAndAllowUserToCreateFilters() throws InterruptedException {
        AssertJUnit.assertTrue(driver.findElement(By.cssSelector("[data-test=\"filter-bar\"]")).isDisplayed());
        driver.findElement(By.cssSelector("[data-test=\"filter-bar\"]")).click();
        driver.findElement(By.cssSelector("[data-test=\"create-filter\"]")).click();
        AssertJUnit.assertTrue(driver.findElement(By.className("ant-modal")).isDisplayed());

        driver.findElement(By.className("ant-modal"))
              .findElement(By.cssSelector("[data-test=\"name-input\"]"))
              .sendKeys("Country name");

        driver.findElement(By.className("ant-modal"))
              .findElement(By.cssSelector("[data-test=\"datasource-input\"]"))
              .sendKeys("wb_health_population");

        WebElement filterShow = driver.findElement(By.cssSelector(
                ".ant-modal [data-test=\"datasource-input\"] .Select__menu"));
        AssertJUnit.assertTrue(filterShow.getText().contains("wb_health_population"));
        filterShow.click();

        driver.findElement(By.className("ant-modal"))
              .findElement(By.cssSelector("[data-test=\"field-input\"]"))
              .sendKeys("country_name");
        driver.findElement(By.className("ant-modal"))
              .findElement(By.cssSelector("[data-test=\"field-input\"]"))
              .sendKeys("" + Keys.ARROW_DOWN + Keys.ARROW_DOWN + Keys.ENTER);
        driver.findElement(By.cssSelector("[data-test=\"apply-changes-instantly-checkbox\"]")).click();
        WebElement saveButton = driver.findElement(By.className("ant-modal-footer"))
                                      .findElement((By.cssSelector("[data-test=\"native-filter-modal-save-button\"]")));
        AssertJUnit.assertTrue(saveButton.isDisplayed());
        saveButton.click();
    }

    @AfterMethod
    public void end() {
        driver.quit();
    }
}
