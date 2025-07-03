package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class TestLogin {

    @Test
    public void runTestLogin() throws Exception {

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        WebDriver driver = new RemoteWebDriver(
                new URL("http://localhost:4444"),
                options
        );

        driver.get("https://dev.aquedi.fr/connexion");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for login input and fill it
        WebElement loginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iLogin")));
        loginInput.sendKeys("usr_stag");  // Replace with your login

        // Wait for password input and fill it
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iPassword")));
        passwordInput.sendKeys("aquedi");  // Replace with your password

        // Wait for the "Se connecter" button and click it
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnConnexion")));
        loginButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h4.text-secondary.pt-2.mt-0.my-0")
        ));

        // Optional: take a screenshot after clicking the login button
        File screenshotDir = new File("src/test/screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename = "screenshot-" + System.currentTimeMillis() + ".png";
        FileHandler.copy(screenshot, new File("src/test/screenshots/" + filename));

        driver.quit();
    }
}
