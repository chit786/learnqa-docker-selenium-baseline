package com.learnqa.specs;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class GlobalFunctions {

    /**
     * clears content of the text field by id
     * @param id id of the element
     */
    public static void clearTextById(String id){
        BaseTest.driver.findElementById(id).click();
        BaseTest.driver.findElementById(id).clear();
    }

    /**
     * clears content of the text field by xpath
     * @param xpath xpath of the element
     */
    public static void clearTextByXpath(String xpath){
        BaseTest.driver.findElementByXPath(xpath).click();
        BaseTest.driver.findElementByXPath(xpath).clear();
    }

    /**
     * types string into text field by id
     * @param id id of the element
     * @param text text to write into text field
     */
    public static void typeTextById(String id, String text){
        BaseTest.driver.findElementById(id).click();
        BaseTest.driver.findElementById(id).sendKeys(text);
    }

    /**
     * types string into text field by xpath
     * @param xpath xpath of the element
     * @param text text to write into text field
     */
    public static void typeTextByXPath(String xpath, String text){
        BaseTest.driver.findElementByXPath(xpath).click();
        BaseTest.driver.findElementByXPath(xpath).sendKeys(text);
    }

    /**
     * capture screenshot at given path
     * @param path full path of the file with name
     */
    public static void getScreenShot(String path) throws IOException {
        File scrFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(path));
    }

    /**
     * capture screenshot at build/screenshots path
     */
    public static void getScreenShot() throws IOException {
        File scrFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);
        String path = "build/screenshots/" + scrFile.getName();
        FileUtils.copyFile(scrFile, new File(path));
    }

}
