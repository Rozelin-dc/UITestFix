package testcases.superset.model_based_dataset.test;

import com.google.gson.JsonObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.AssertJUnit;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OriginalWebDriver extends ChromeDriver {
    public OriginalWebDriver(ChromeOptions options) {
        super(options);
    }

    public OriginalWebDriver login() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL("/login/").openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        JsonObject json = new JsonObject();
        json.addProperty("username", "admin");
        json.addProperty("password", "general");
        String jsonString = json.toString();
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = conn.getResponseCode();
        AssertJUnit.assertEquals(200, responseCode);
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
