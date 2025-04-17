package autorepair.instrument.newprocess;

import autorepair.match.MatchFactory;
import autorepair.state.edge.Event;
import autorepair.state.statematchine.StateMachineImpl;
import autorepair.state.vertex.StateVertex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.UtilsDomNode;
import utils.UtilsProperties;
import utils.UtilsSeleniumHelper;
import utils.UtilsXpath;

import java.io.File;

public class NewWebDriverProcess {

    public static Object findElementProcess(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            WebDriver driver,
            ProceedingJoinPoint proceedingJoinPoint
    ) throws Throwable {
        WebElement webElement = null;
        Event oldEvent = oldStateMachine.getScriptSequence()
                                        .getEvent(
                                                proceedingJoinPoint.getSourceLocation().toString(),
                                                proceedingJoinPoint.getSignature().getName()
                                        );
        try {
            webElement = (WebElement) proceedingJoinPoint.proceed();
            if (!MatchFactory.isBroken(oldStateMachine, newStateMachine, oldEvent, driver, webElement)) {
                return webElement;
            }
        } catch (Exception e) {

        }
        // collect the web page state.
        StateVertex sourceStateVertex = newStateMachine.collectPageData(driver);
        newStateMachine.setSourceStateVertex(sourceStateVertex);
        System.out.println(newStateMachine.getSavePath());
        String newXpath = "";
        boolean repair = false;
        try {
            webElement = (WebElement) proceedingJoinPoint.proceed();
        } catch (NoSuchElementException noSuchElementException) {
            // replace the locator.
            System.out.println("could not find target element.");
            if (Boolean.parseBoolean(UtilsProperties.getConfigProperties().getProperty("repair"))) {
                System.out.println(noSuchElementException.getMessage());
                String method = "findElement";
                newXpath = MatchFactory.match(oldStateMachine, newStateMachine, oldEvent, driver);
                System.out.println("the repaired result:" + newXpath);
                webElement = driver.findElement(By.xpath(newXpath));
            } else {
                System.out.println("No repairs are performed.");
                throw noSuchElementException;
            }
        }
        String absoluteXpath = UtilsXpath.generateXPathForWebElement(webElement, "");
        // collect the web page state.
//        StateVertex targetStateVertex = newStateMachine.collectPageData(driver);
//        newStateMachine.setTargetStateVertex(targetStateVertex);
        newStateMachine.setTargetStateVertex(newStateMachine.getSourceStateVertex());
        labelElement(repair, newStateMachine, webElement);
        int index = UtilsDomNode.getElementByAbsoluteXpath(
                absoluteXpath,
                newStateMachine.getSavePath() +
                sourceStateVertex.getStateVertexId() +
                File.separator
        );
        UtilsSeleniumHelper.captureScreen(
                webElement,
                newStateMachine.getSavePath() +
                newStateMachine.getSourceStateVertex().getStateVertexId() +
                File.separator +
                index +
                ".png"
        );
        // add event.
        Event event = newStateMachine.addWebDriverFindElementEvent(
                driver,
                proceedingJoinPoint,
                absoluteXpath,
                webElement
        );
        newStateMachine.addEvent2ScriptSequence(event);
        // return the found element.
        return webElement;
    }

    public static void labelElement(Boolean repair, StateMachineImpl newStateMachine, WebElement webElement) {
        if (repair) {
            UtilsSeleniumHelper.labelScreenBlue(
                    newStateMachine.getSavePath() +
                    newStateMachine.getSourceStateVertex().getStateVertexId() +
                    File.separator +
                    "fullScreen.png",
                    webElement,
                    newStateMachine.getSavePath() +
                    "event" +
                    File.separator +
                    newStateMachine.getScriptSequence().getEdges().size() +
                    ".png"
            );
        } else {
            UtilsSeleniumHelper.labelScreenRed(
                    newStateMachine.getSavePath() +
                    newStateMachine.getSourceStateVertex().getStateVertexId() +
                    File.separator +
                    "fullScreen.png",
                    webElement,
                    newStateMachine.getSavePath() +
                    "event" +
                    File.separator +
                    newStateMachine.getScriptSequence().getEdges().size() +
                    ".png"
            );
        }
    }

}
