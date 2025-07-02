//package com.example;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.time.Duration;
//
//import org.junit.jupiter.api.*;
//import org.openqa.selenium.By;
//import org.openqa.selenium.PageLoadStrategy;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.w3c.dom.Document;
//
//public class FirstScriptTest {
//
//    static WebDriver driver;
//
//    @BeforeAll
//    public static void setup() throws MalformedURLException {
//        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
//
//        driver = new RemoteWebDriver(
//                new URL("http://localhost:4444"),
//                firefoxOptions
//        );
//    }
//
//
//    @Test
//    public void eightComponents() {
//
//        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
//
//        String title = driver.getTitle();
//        assertEquals("Web form", title);
//
//        WebElement textBox = driver.findElement(By.name("my-text"));
//        WebElement submitButton = driver.findElement(By.cssSelector("button"));
//
//        textBox.sendKeys("Selenium");
//        submitButton.click();
//
//        WebElement message = driver.findElement(By.id("message"));
//        String value = message.getText();
//        assertEquals("Received!", value);
//
//    }
//
//    @AfterAll
//    public static void teardown() {
//        driver.quit(); // closes browser once after all tests
//    }
//
//
//}
