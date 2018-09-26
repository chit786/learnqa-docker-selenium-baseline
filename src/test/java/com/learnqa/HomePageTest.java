package com.learnqa;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.learnqa.specs.BaseTest;

import com.learnqa.pages.*;

public class HomePageTest extends BaseTest {
    @BeforeMethod
    public void testStartUp(){
        HomePage.openMe()
                .closeRobot();
    }

    @Test
    public void searchSelenium() {
        HomePage.searchBy("selenium");
    }

    @Test
    public void searchZalenium() {
        HomePage.searchBy("zalenium");
    }
}
