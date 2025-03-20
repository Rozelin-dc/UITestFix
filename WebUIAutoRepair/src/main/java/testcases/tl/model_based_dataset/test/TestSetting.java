package testcases.tl.model_based_dataset.test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
import utils.UtilsSeleniumHelper;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class TestSetting {
    WebDriver driver;

    @BeforeMethod
    public void before() throws IOException {
        System.setProperty("webdriver.chrome.driver",
                DriverConfig.DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=ja");
//        options.addArguments("--encoding=Shift_JIS");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(Constants.getTLUrl());
    }

//    @Test(priority = 0)
//    void testCheckButtonCount() throws InterruptedException {
//        int iconButtonCount = driver.findElements(By.className("icon-button")).size();
//        assertEquals(3, iconButtonCount);
//    }

    @Test(priority = 0)
    void testCaption() throws InterruptedException {
        WebElement caption = driver.findElement(By.className("caption"));
        assertTrue(caption.getText().contains("ローマ字"));
    }
    
    @AfterMethod
    public void end () {
        driver.quit();
    }

}
