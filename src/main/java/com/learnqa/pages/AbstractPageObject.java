package com.learnqa.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.learnqa.specs.BaseTest;

/**
 * Created by chitrang on 18/02/17.
 */
public abstract class AbstractPageObject {

    protected static WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor for abstract page object class
     * All the pages must extend this
     * @param driver Webdriver
     */
    public AbstractPageObject(WebDriver driver){
        this.driver = driver;
        this.wait = (new WebDriverWait(driver, 30));
        isLoaded();
    }

    // Each page object must implement this method to return the identifier of a unique WebElement on that page.
    // The presence of this unique element will be used to assert that the expected page has finished loading
    protected abstract By getUniqueElement();

    protected void isLoaded() throws Error {
        //Define a list of WebElements that match the unique element locator for the page
        if(getUniqueElement()!=null){
            List<WebElement> uniqueElement = this.driver.findElements(getUniqueElement());

            // Assert that the unique element is present in the DOM
            Assert.assertTrue((uniqueElement.size() > 0), "Unique Element \'" + getUniqueElement().toString() + "\' not found for " + this.getClass().getSimpleName());

            // Wait until the unique element is visible in the browser and ready to use. This helps make sure the page is
            // loaded before the next step of the tests continue.
            wait.until(ExpectedConditions.visibilityOfElementLocated(getUniqueElement()));
        }


    }




}