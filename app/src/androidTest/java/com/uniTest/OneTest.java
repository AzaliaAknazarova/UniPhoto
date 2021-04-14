package com.uniTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class OneTest {
    static AppiumDriver<MobileElement> driver;

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        //Set up desired capabilities and pass the Android app-activity and app-package to Appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "9");
        capabilities.setCapability("deviceName","Nexus 5 API 28");
        capabilities.setCapability("udid", "emulator-5554");

        capabilities.setCapability("appPackage", "com.example.uniphoto");
        capabilities.setCapability("appActivity","com.example.uniphoto.ui.activity.MainActivity"); // This is Launcher activity of your app (you can get it from apk info app)

        driver = new AndroidDriver(new URL("http://localhost:0.0.0.0:4723/"), capabilities);

    }

    @Test
    public void testCal() throws Exception {
        MobileElement el1 = (MobileElement) driver.findElementById("com.example.uniphoto:id/userNameValue");
        el1.sendKeys("nastya");
        MobileElement el2 = (MobileElement) driver.findElementById("com.example.uniphoto:id/passwordValue");
        el2.sendKeys("06021991Na!");
        MobileElement el3 = (MobileElement) driver.findElementById("com.example.uniphoto:id/signInButton");
        el3.click();
/*
        //locate the Text on the calculator by using By.name()
        WebElement two=driver.findElement(By.name("2"));
        two.click();
        WebElement plus=driver.findElement(By.name("+"));
        plus.click();
        WebElement four=driver.findElement(By.name("4"));
        four.click();
        WebElement equalTo=driver.findElement(By.name("="));
        equalTo.click();
        //locate the edit box of the calculator by using By.tagName()
        WebElement results=driver.findElement(By.tagName("EditText"));
        //Check the calculated value on the edit box
        assert results.getText().equals("6"):"Actual value is : "+results.getText()+" did not match with expected value: 6";
*/
    }

   /* @AfterClass
    public static void teardown(){
        //close the app
        driver.quit();
    }*/
}



