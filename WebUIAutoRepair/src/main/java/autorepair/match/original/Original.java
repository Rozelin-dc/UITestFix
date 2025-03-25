package autorepair.match.original;

import autorepair.state.datacollect.PreDomNodeInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Original {
    static final String TEST_ID_ATTRIBUTE_NAME = "data-test-id";

    public static WebElement retrieveWebElementFromDOMInfo(
            WebDriver driver,
            PreDomNodeInfo oldPreDomNodeInfo,
            String eventName
    ) {
        String testId = oldPreDomNodeInfo.getAttributes().get(TEST_ID_ATTRIBUTE_NAME);

        if (testId == null || testId.isEmpty()) {
            return null;
        }

        WebElement el = null;
        try {
            el = driver.findElement(By.cssSelector("[" + TEST_ID_ATTRIBUTE_NAME + "]=" + testId));
            return el;
        } catch (Exception ignored) {
        }

        try {
            el = driver.findElement(By.cssSelector("[" +
                                                   TEST_ID_ATTRIBUTE_NAME +
                                                   "-for-action-" +
                                                   eventName +
                                                   "]=" +
                                                   testId));
        } catch (Exception ignored) {
        }

        return el;
    }
}
