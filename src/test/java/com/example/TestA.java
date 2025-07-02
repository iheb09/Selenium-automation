package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class TestA {
    @Test
    public void runTestA() throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");  // Headless mode enabled - run without ui
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); //DOM access is ready, but other resources like images may still be loading


        WebDriver driver = new RemoteWebDriver(
                new URL("http://localhost:4444"),  // Change to your Selenium Grid URL if needed
                options
        );
        driver.get("https://example.com");
        System.out.println("TestA Title: " + driver.getTitle());
        driver.quit();
    }
}
