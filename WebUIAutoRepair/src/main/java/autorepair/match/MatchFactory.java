package autorepair.match;

import autorepair.match.original.Original;
import autorepair.match.sftm.SFTM;
import autorepair.match.sftm2023.SFTM2023;
import autorepair.match.vista.VISTA;
import autorepair.match.water.WATER;
import autorepair.match.webevo.WEBEVO;
import autorepair.state.datacollect.JsonProcess;
import autorepair.state.datacollect.PreDomNodeInfo;
import autorepair.state.edge.Event;
import autorepair.state.statematchine.StateMachineImpl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.UtilsProperties;
import utils.UtilsSeleniumHelper;
import utils.UtilsTxtLoader;
import utils.UtilsXpath;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MatchFactory {
    protected static String _matchMethod = null;

    protected static String _getMatchMethod() {
        if (_matchMethod == null || _matchMethod.isEmpty()) {
            try {
                _matchMethod = UtilsProperties.getConfigProperties().getProperty("match").trim();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                _matchMethod = "water";
            }
        }
        return _matchMethod;
    }

    public static boolean isBroken(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent,
            WebDriver driver,
            WebElement webElement
    ) throws IOException {
        String matchMethod = _getMatchMethod();
        switch (matchMethod) {
            case "original":
                return isBrokenByOriginal(oldStateMachine, newStateMachine, oldEvent, webElement);
            default:
                return false;
        }
    }

    public static boolean isBrokenByOriginal(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent,
            WebElement webElement
    ) throws IOException {
        List<PreDomNodeInfo> oldPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(oldStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        oldEvent.getSourceVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        PreDomNodeInfo preDomNodeInfo = null;
        for (PreDomNodeInfo temp : oldPreDomNodeInfoList) {
            if (temp.getXpath().equals(oldEvent.getAbsoluteXpath())) {
                preDomNodeInfo = temp;
                break;
            }
        }
        if (preDomNodeInfo == null) {
            return true;
        }
        return Original.checkIsBrokenFromDOMInfo(
                newStateMachine.getDriver(),
                preDomNodeInfo,
                oldEvent.getMethod(),
                webElement
        );
    }

    public static String match(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent,
            WebDriver driver
    ) throws IOException {
        String matchMethod = _getMatchMethod();
        System.out.println("execute match method: " + matchMethod);
        switch (matchMethod) {
            case "sftm":
                return matchBySFTM(oldStateMachine, oldEvent, driver);
            case "sftm2023":
                return matchBySFTM2023(oldStateMachine, newStateMachine, oldEvent, driver);
            case "vista":
                return matchByVista(oldStateMachine, newStateMachine, oldEvent, driver);
            case "webevo":
                return matchByWebevo(driver, oldStateMachine, newStateMachine, oldEvent);
            case "original":
                return matchByOriginal(oldStateMachine, newStateMachine, oldEvent);
            default:
                // water(default)
                return matchByWATER(oldStateMachine, newStateMachine, oldEvent);
        }
    }

    public static String matchByVista(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent,
            WebDriver driver
    ) throws IOException {
        String newFullScreenPath = newStateMachine.getSavePath() +
                                   newStateMachine.getSourceStateVertex().getStateVertexId() +
                                   File.separator +
                                   "fullScreen.png";
        String oldFullScreenPath = oldStateMachine.getSavePath() +
                                   File.separator +
                                   oldEvent.getSourceVertexId() +
                                   File.separator +
                                   "fullScreen.png";
        List<PreDomNodeInfo> oldPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(oldStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        oldEvent.getSourceVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        PreDomNodeInfo preDomNodeInfo = null;
        for (PreDomNodeInfo temp : oldPreDomNodeInfoList) {
            if (temp.getXpath().equals(oldEvent.getAbsoluteXpath())) {
                preDomNodeInfo = temp;
                break;
            }
        }
        return VISTA.retrieveDomNode(oldFullScreenPath, newFullScreenPath, preDomNodeInfo, driver);
    }

    public static String matchBySFTM(
            StateMachineImpl oldStateMachine,
            Event oldEvent,
            WebDriver driver
    ) throws IOException {
        String oldHtml = UtilsTxtLoader.readFile02(oldStateMachine.getSavePath() +
                                                   File.separator +
                                                   oldEvent.getSourceVertexId() +
                                                   File.separator +
                                                   "temp.html");
        String newHtml = UtilsSeleniumHelper.getHtml(driver);
        SFTM sftm = new SFTM();

        sftm.match(oldHtml, newHtml);

        return sftm.getNewXpath(oldEvent.getAbsoluteXpath());
    }

    public static String matchBySFTM2023(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent,
            WebDriver driver
    ) throws IOException {
        String oldHtml = UtilsTxtLoader.readFile02(oldStateMachine.getSavePath() +
                                                   File.separator +
                                                   oldEvent.getSourceVertexId() +
                                                   File.separator +
                                                   "temp.html");

        SFTM2023 sftm = new SFTM2023();
        sftm.match(
                oldStateMachine.getSavePath() +
                File.separator +
                oldEvent.getSourceVertexId() +
                File.separator +
                "temp.html",
                newStateMachine.getSavePath() +
                File.separator +
                newStateMachine.getSourceStateVertex().getStateVertexId() +
                File.separator +
                "temp.html"
        );

        return sftm.getNewXpath(oldEvent.getAbsoluteXpath());
    }

    public static String matchByWATER(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent
    ) throws IOException {
        List<PreDomNodeInfo> oldPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(oldStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        oldEvent.getSourceVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        String oldHtml = UtilsTxtLoader.readFile02(oldStateMachine.getSavePath() +
                                                   File.separator +
                                                   oldEvent.getSourceVertexId() +
                                                   File.separator +
                                                   "temp.html");
        List<PreDomNodeInfo> newPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(newStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        newStateMachine.getSourceStateVertex()
                                                                                                       .getStateVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        PreDomNodeInfo preDomNodeInfo = null;
        for (PreDomNodeInfo temp : oldPreDomNodeInfoList) {
            if (temp.getXpath().equals(oldEvent.getAbsoluteXpath())) {
                preDomNodeInfo = temp;
                break;
            }
        }
        if (preDomNodeInfo == null) {
            return "";
        }
        WebElement webElement = WATER.retrieveWebElementFromDOMInfo(
                newStateMachine.getDriver(),
                preDomNodeInfo,
                oldHtml,
                newPreDomNodeInfoList
        );
        if (webElement != null) {
            return UtilsXpath.generateXPathForWebElement(webElement, "");
        } else {
            return "";
        }
    }

    public static String matchByWebevo(
            WebDriver driver,
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent
    ) throws IOException {
        List<PreDomNodeInfo> newPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(newStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        newStateMachine.getSourceStateVertex()
                                                                                                       .getStateVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        List<PreDomNodeInfo> oldPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(oldStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        oldEvent.getSourceVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        String targetText = oldPreDomNodeInfoList.get(oldEvent.getElementId()).getText();
        int index = oldEvent.getElementId();
        String targetPath = oldStateMachine.getSavePath() +
                            oldEvent.getSourceVertexId() +
                            File.separator +
                            index +
                            ".png";
        System.out.println(targetPath);
        System.out.println(newStateMachine.getSavePath() +
                           File.separator +
                           newStateMachine.getSourceStateVertex().getStateVertexId() +
                           File.separator +
                           "preDomNodeInfo.json");
        String candidateLocation = newStateMachine.getSavePath() +
                                   File.separator +
                                   newStateMachine.getSourceStateVertex().getStateVertexId() +
                                   File.separator +
                                   "candidate" +
                                   File.separator;
        WEBEVO webevo = new WEBEVO();
        String xpath = webevo.processingCandidatesImage(
                driver,
                candidateLocation,
                newPreDomNodeInfoList,
                targetText,
                targetPath
        );

        return xpath;
    }

    public static String matchByOriginal(
            StateMachineImpl oldStateMachine,
            StateMachineImpl newStateMachine,
            Event oldEvent
    ) throws IOException {
        List<PreDomNodeInfo> oldPreDomNodeInfoList = JsonProcess.readPreDomNodeInfoJson(oldStateMachine.getSavePath() +
                                                                                        File.separator +
                                                                                        oldEvent.getSourceVertexId() +
                                                                                        File.separator +
                                                                                        "preDomNodeInfo.json");
        PreDomNodeInfo preDomNodeInfo = null;
        for (PreDomNodeInfo temp : oldPreDomNodeInfoList) {
            if (temp.getXpath().equals(oldEvent.getAbsoluteXpath())) {
                preDomNodeInfo = temp;
                break;
            }
        }
        if (preDomNodeInfo == null) {
            return "";
        }
        WebElement webElement = Original.retrieveWebElementFromDOMInfo(
                newStateMachine.getDriver(),
                preDomNodeInfo,
                oldEvent.getMethod()
        );
        if (webElement != null) {
            return UtilsXpath.generateXPathForWebElement(webElement, "");
        } else {
            return "";
        }
    }
}
