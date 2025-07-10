package Formulaires.Tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class TestP03T001 {

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
    public void runTestP02T001() throws Exception {
        WebDriver driver = null;

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            driver.manage().window().setSize(new Dimension(1920, 1080));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            System.out.println("Running TestP02T002-T007");

            driver.get("https://dev.aquedi.fr/connexion");

            Properties creds = loadCredentials();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iLogin")))
                    .sendKeys(creds.getProperty("username"));
            driver.findElement(By.id("iPassword"))
                    .sendKeys(creds.getProperty("password"));
            driver.findElement(By.id("btnConnexion")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Tableau de bord']")));

            WebElement fermerBtn1 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'btn') and .//span[normalize-space(text())='Fermer']]")
            ));

            fermerBtn1.click();
            takeScreenshot(driver,"1", "accessing_application");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'btnDashboard') and .//label[text()='Formulaires']]"))).click();

            WebElement nodeToDoubleClick = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'node-content-wrapper') and .//span[normalize-space()='test_iheb']]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick);
            new Actions(driver).doubleClick(nodeToDoubleClick).perform();
            Thread.sleep(500);
            takeScreenshot(driver,"2", "after_clicking_test_iheb_folder");
            // 1. Click "Créer un Formulaire" button
            WebElement createFormBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(normalize-space(), 'Créer un Formulaire')]]")
            ));
            createFormBtn.click();

            // 2. Wait for the modal to appear
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'modal-content')]//span[contains(text(), 'Créer un nouveau formulaire')]")
            ));

            // 3. Fill "Nom"
            WebElement nomInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[formcontrolname='nomCtrl']")));
            nomInput.sendKeys("Formulaire Selenium");

            // 4. Fill "Description"
            WebElement descInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("textarea[formcontrolname='descriptionCtrl']")));
            descInput.sendKeys("Automated form created via Selenium");

            // 5. Select "1 minute" from Fréquence dropdown
            WebElement freqDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("select[formcontrolname='frequencyCtrl']")));
            Select select = new Select(freqDropdown);
            select.selectByVisibleText("1 minute");

            // 6. Select "Groupe_test_FORMULAIRE" in Groupes de formulaires (typeahead input + Enter)
            WebElement groupInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("ng-select[formcontrolname='groupeCtrl'] input[role='combobox']")));
            groupInput.click();
            groupInput.sendKeys("Groupe_test_FORMULAIRE");
            Thread.sleep(500); // wait for suggestions to appear
            groupInput.sendKeys(Keys.ENTER);

            // Optional: take screenshot here if needed
             takeScreenshot(driver, "3", "formulaire_ready");

            // Wait until the "Valider" button becomes enabled (not disabled)
            WebElement validerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Valider') and not(@disabled)]")
            ));

            // Click Valider
            validerBtn.click();
            Thread.sleep(1000);
            takeScreenshot(driver, "4", "formulaire_crée");




        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
