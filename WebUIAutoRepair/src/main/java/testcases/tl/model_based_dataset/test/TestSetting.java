package testcases.tl.model_based_dataset.test;

import config.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.testng.annotations.*;
import testcases.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static org.testng.AssertJUnit.*;

public class TestSetting {
    WebDriver driver;

    @BeforeMethod
    public void before() throws IOException {
        System.setProperty("webdriver.chrome.driver", DriverConfig.DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=ja");
//        options.addArguments("--encoding=Shift_JIS");
        options.addArguments("--headless");
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
        assertTrue(caption.getText().contains("UTAWO UTAOU"));
    }

    @Test(priority = 0)
    void openSettingDialog() throws InterruptedException {
        List<WebElement> iconButtons = driver.findElements(By.className("icon-button"));
        WebElement settingButton = iconButtons.get(2);
        settingButton.click();
    }

    @AfterMethod
    public void end() {
        driver.quit();
    }

}
