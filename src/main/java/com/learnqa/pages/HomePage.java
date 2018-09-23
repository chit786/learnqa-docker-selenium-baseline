package com.learnqa.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.learnqa.specs.BaseTest;

public class HomePage extends AbstractPageObject {

    public HomePage() {
        super(BaseTest.getDriver());
    }

    @Override
    protected By getUniqueElement() {
        return By.id("search_form_input_homepage");
    }

    public static HomePage openMe(){
        BaseTest.getDriver().get(BaseTest.WEB_SERVER);
        return new HomePage();
    }

    public static HomePage closeRobot(){
        List<WebElement> el = BaseTest.getDriver().findElementsByXPath("//span[@class='ddgsi badge-link__close js-badge-link-dismiss']");
        if(el.size() > 0 && el.get(0).isDisplayed()) {
            el.get(0).click();
        }
        return new HomePage();
    }

    public static SearchResultPage searchBy(String text){
        BaseTest.getDriver().findElementById("search_form_input_homepage").sendKeys(text);
        BaseTest.getDriver().findElementById("search_button_homepage").click();
        return new SearchResultPage();
    }

    public static void rightMenuVisible(){
        BaseTest.getDriver().findElementByXPath("//a[contains(@class,'js-side-menu-open')]").click();
        BaseTest.getDriver().findElementByXPath("//div[contains(@class,'nav-menu--slideout is-open')]").isDisplayed();
    }
}
