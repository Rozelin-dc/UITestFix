package autorepair.match.original;

import autorepair.state.datacollect.*;
import org.openqa.selenium.*;

public class Original {
    static final String TEST_ID_ATTRIBUTE_NAME = "data-test-id";

    public static WebElement retrieveWebElementFromDOMInfo(WebDriver driver, PreDomNodeInfo oldPreDomNodeInfo) {
        String testId = oldPreDomNodeInfo.getAttributes().get(TEST_ID_ATTRIBUTE_NAME);
        System.out.println("testId: " + testId);

        if (testId == null || testId.isEmpty()) {
            return null;
        }

        WebElement el = null;
        try {
            el = driver.findElement(By.cssSelector("[" + TEST_ID_ATTRIBUTE_NAME + "=\"" + testId + "\"]"));
            return el;
        } catch (Exception ignored) {
        }

        try {
            el = driver.findElement(By.cssSelector("[" +
                                                   TEST_ID_ATTRIBUTE_NAME +
                                                   "-for-action-" +
                                                   eventName +
                                                   "=\"" +
                                                   testId +
                                                   "\"]"));
        } catch (Exception ignored) {
        }

        return el;
    }
}
