package MessageConfig.Tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class TestP09 {

    private Properties loadCredentials() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("credentials.properties"));
        return props;
    }

    private String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    private void takeScreenshot(WebDriver driver, String stepNumber, String name) throws Exception {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileHandler.copy(srcFile, new File("src/test/screenshots/" + stepNumber + "_" + name + "_" + timestamp() + ".png"));
    }

    @Test
    public void runTestP02T006() throws Exception {
        WebDriver driver = null;

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            driver.manage().window().setSize(new Dimension(1920, 1080));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            System.out.println("Running Test P09");

            driver.get("https://dev.aquedi.fr/connexion");

            Properties creds = loadCredentials();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iLogin")))
                    .sendKeys(creds.getProperty("username"));
            driver.findElement(By.id("iPassword"))
                    .sendKeys(creds.getProperty("password"));
            driver.findElement(By.id("btnConnexion")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Tableau de bord']")));

//            WebElement fermerBtn1 = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//button[contains(@class, 'btn') and .//span[normalize-space(text())='Fermer']]")
//            ));
//
//            fermerBtn1.click();

            takeScreenshot(driver,"1","Accessing_site");

            WebElement gearIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("dropdownMenu3")
            ));
            gearIcon.click();

            // Step 2: Wait for and click on "Configurations des messages"
            WebElement configMessagesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='dMenuParam']//button[contains(., \"Configurations des messages\")]")
            ));
            configMessagesBtn.click();

            Thread.sleep(500);
            takeScreenshot(driver,"2","Accessing_config_messages");

            // Step 1: Click the "Configurer un message" button
            WebElement configBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[span[contains(text(), 'Configurer un message')]]")
            ));
            configBtn.click();

            // Wait for modal
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.modal-dialog")
            ));

            // Fill "Nom"
            modal.findElement(By.cssSelector("input[formcontrolname='nom']")).sendKeys("Test Message");

                // --- Date de début ---
            // Open calendar for date_debut
            WebElement dateDebutInput = modal.findElement(
                    By.cssSelector("ng2-flatpickr[formcontrolname='date_debut'] input")
            );
            dateDebutInput.click();

            // Wait for visible calendar
            WebElement calendarDebut = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.flatpickr-calendar.open")
            ));

            // Find and click the 20th inside *that* calendar
            WebElement debutDateToClick = calendarDebut.findElement(
                    By.xpath(".//span[contains(@class, 'flatpickr-day') and text()='20' and not(contains(@class,'prevMonthDay')) and not(contains(@class,'nextMonthDay'))]")
            );
            wait.until(ExpectedConditions.elementToBeClickable(debutDateToClick)).click();


            WebElement dateFinInput = modal.findElement(
                    By.cssSelector("ng2-flatpickr[formcontrolname='date_fin'] input")
            );
            dateFinInput.click();

            WebElement calendarFin = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.flatpickr-calendar.open")
            ));

            WebElement finDateToClick = calendarFin.findElement(
                    By.xpath(".//span[contains(@class, 'flatpickr-day') and text()='21' and not(contains(@class,'prevMonthDay')) and not(contains(@class,'nextMonthDay'))]")
            );
            wait.until(ExpectedConditions.elementToBeClickable(finDateToClick)).click();


            // --- Description ---
            WebElement descEditor = modal.findElement(
                    By.cssSelector("quill-editor .ql-editor")
            );
            descEditor.click();
            descEditor.sendKeys("Ceci est un message de test.");
            takeScreenshot(driver,"3","Form_config_message_filled");
            WebElement validerBtn = modal.findElement(
                    By.xpath(".//button[contains(text(), 'Valider') and not(@disabled)]")
            );
            wait.until(ExpectedConditions.elementToBeClickable(validerBtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(configBtn));
            takeScreenshot(driver,"4","Message_configured");




        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}