package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class TestB {
    @Test
    public void runTestB() throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");  // Headless mode enabled - runs without ui
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); //DOM access is ready, but other resources like images may still be loading

        WebDriver driver = new RemoteWebDriver(
                new URL("http://localhost:4444"),
                options
        );
        driver.get("https://www.selenium.dev");
        System.out.println("TestB Title: " + driver.getTitle());
        driver.quit();
    }
}
