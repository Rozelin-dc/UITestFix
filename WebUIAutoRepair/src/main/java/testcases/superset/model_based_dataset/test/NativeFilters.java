package testcases.superset.model_based_dataset.test;

import config.DriverConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testcases.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertTrue;

public class NativeFilters {
    WebDriver driver;

    @BeforeMethod
    public void before() throws IOException {
        System.setProperty("webdriver.chrome.driver", DriverConfig.DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(Constants.getSupersetUrl());
    }

    @Test(priority = 0)
    void shouldShowFilterBarAndAllowUserToCreateFilters() throws InterruptedException {
        assertTrue(driver.findElement(By.cssSelector("[data-test=\"filter-bar\"]")).isDisplayed());
        driver.findElement(By.cssSelector("[data-test=\"filter-bar\"]")).click();
        driver.findElement(By.cssSelector("[data-test=\"create-filter\"]")).click();
        assertTrue(driver.findElement(By.className("ant-modal")).isDisplayed());

        driver.findElement(By.className("ant-modal")).findElement(By.cssSelector("[data-test=\"name-input\"]")).sendKeys("Country name");

        driver.findElement(By.className("ant-modal")).findElement(By.cssSelector("[data-test=\"datasource-input\"]")).sendKeys("wb_health_population");

        WebElement filterShow = driver.findElement(By.cssSelector(".ant-modal [data-test=\"datasource-input\"] .Select__menu"));
        assertTrue(filterShow.getText().contains("wb_health_population"));
        filterShow.click();

        driver.findElement(By.className("ant-modal")).findElement(By.cssSelector("[data-test=\"field-input\"]")).sendKeys("country_name");
        driver.findElement(By.className("ant-modal")).findElement(By.cssSelector("[data-test=\"field-input\"]")).sendKeys("{downarrow}{downarrow}{enter}");
        driver.findElement(By.cssSelector("[data-test=\"apply-changes-instantly-checkbox\"]")).click();
        WebElement saveButton = driver.findElement(By.className("ant-modal-footer")).findElement((By.cssSelector("[data-test=\"native-filter-modal-save-button\"]")));
        assertTrue(saveButton.isDisplayed());
        saveButton.click();
    }

    @AfterMethod
    public void end() {
        driver.quit();
    }
}
