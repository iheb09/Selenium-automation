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

public class TestP03T002 {

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
    public void runTestP02T002() throws Exception {
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



            // 1. Click "Importer un Formulaire"
            WebElement importBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(normalize-space(), 'Importer un Formulaire')]]")
            ));
            importBtn.click();

            // 2. Wait for modal title to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'modal-content')]//span[contains(text(), 'Importer un formulaire')]")
            ));

            // 3. Upload the file
            URL resource = getClass().getClassLoader().getResource("formulaire.xlsx");
            if (resource == null) {
                throw new RuntimeException("Fichier rapport.xlsx introuvable dans les resources.");
            }
            File fileToUpload = new File(resource.toURI());
            System.out.println("Uploading file: " + fileToUpload.getAbsolutePath());

            WebElement fileInput = driver.findElement(By.id("fileInput"));
            fileInput.sendKeys(fileToUpload.getAbsolutePath());

            // 4. Select "Groupe_test_FORMULAIRE"
            WebElement groupeSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("groupeInput")));
            Select groupeDropdown = new Select(groupeSelect);
            groupeDropdown.selectByVisibleText("Groupe_test_FORMULAIRE");

            // 5. Select "test_iheb"
            WebElement dossierSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("dossierInput")));
            Select dossierDropdown = new Select(dossierSelect);
            Thread.sleep(500);
            dossierDropdown.selectByVisibleText("test_iheb");
            takeScreenshot(driver, "5", "formulaire_importé_rdy");

//            // 6. Wait for "Importer" button to be enabled and click
            WebElement importFinalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Importer') and not(@disabled)]")
            ));
            importFinalBtn.click();
            Thread.sleep(1000);
            takeScreenshot(driver,"6", "formulaire_importé_crée");




        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
