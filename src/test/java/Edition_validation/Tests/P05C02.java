package Edition_validation.Tests;

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

public class P05C02 {

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

            // Locate and click the green "Edition & validation" button by visible label text
            WebElement editionButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'btnDashboard') and .//label[contains(text(), 'Edition')]]")
            ));
            editionButton.click();

            WebElement dossiersCard = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'card-body') and .//h5[text()='Dossiers de variables']]")
            ));
            dossiersCard.click();
            WebElement creerDossierBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Créer un dossier' and .//span[contains(text(), 'Créer un dossier')]]")
            ));
            creerDossierBtn.click();
            takeScreenshot(driver,"2","Creer_dossier_modal");
            // 1. Press on the first combobox ("Type de dossier à créer")
            WebElement folderTypeCombo = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//select[@formcontrolname='folderTypeCtrl']")
            ));
            folderTypeCombo.click(); // Open dropdown
            folderTypeCombo.sendKeys(Keys.ARROW_DOWN);
            folderTypeCombo.sendKeys(Keys.ARROW_DOWN);
            folderTypeCombo.sendKeys(Keys.ENTER);

            // 2. Fill in the "Nom" field
            WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@formcontrolname='nameFolderCtrl']")
            ));
            nameInput.sendKeys("Test_iheb");

            // 3. Press on the second combobox ("Site"), then press Enter
            WebElement siteComboInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ng-select[@formcontrolname='siteCtrl']//input[@type='text']")
            ));
            siteComboInput.click(); // Focus input
            Thread.sleep(500); // allow dropdown to load
            siteComboInput.sendKeys(Keys.ENTER);

            WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(., 'Créer') and not(@disabled)]")
            ));
            createButton.click();
            Thread.sleep(1000);
            takeScreenshot(driver,"3","Creer_dossier_done");



        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}