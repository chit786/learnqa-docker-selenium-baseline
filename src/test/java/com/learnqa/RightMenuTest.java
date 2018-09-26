package com.learnqa;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.learnqa.pages.HomePage;
import com.learnqa.specs.BaseTest;

public class RightMenuTest extends BaseTest {
    @BeforeMethod
    public void testStartUp(){
        HomePage.openMe()
                .closeRobot();
    }

    @Test
    public void socialSharingTest() {
        HomePage.rightMenuVisible();
    }
}
