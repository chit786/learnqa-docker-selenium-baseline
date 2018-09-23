package com.learnqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.learnqa.specs.BaseTest;

public class SearchResultPage extends AbstractPageObject{

    public SearchResultPage() {
        super(BaseTest.getDriver());
    }

    @Override
    protected By getUniqueElement() {
        return By.className("results--main");
    }
}
