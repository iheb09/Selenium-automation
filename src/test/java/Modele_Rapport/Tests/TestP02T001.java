package Modele_Rapport.Tests;

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

public class TestP02T001 {

    private Properties loadCredentials() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("credentials.properties"));
        return props;
    }

    private String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    private void takeScreenshot(WebDriver driver, String name) throws Exception {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileHandler.copy(srcFile, new File("src/test/screenshots/" + name + "_" + timestamp() + ".png"));
    }

    @Test
    public void runTestP02T001() throws Exception {
        WebDriver driver = null;

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);

            // Enable remote file uploads
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            System.out.println("Running TestP02T001");

            driver.get("https://dev.aquedi.fr/connexion");

            Properties creds = loadCredentials();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iLogin")))
                    .sendKeys(creds.getProperty("username"));
            driver.findElement(By.id("iPassword"))
                    .sendKeys(creds.getProperty("password"));
            driver.findElement(By.id("btnConnexion")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Tableau de bord']")));
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[text()='Fermer']]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'btnDashboard') and .//label[text()='Modèles & Rapports']]"))).click();

            WebElement nodeToDoubleClick = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'node-content-wrapper') and .//span[normalize-space()='test_iheb']]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick);
            new Actions(driver).doubleClick(nodeToDoubleClick).perform();

            WebElement createExcelButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(normalize-space(), 'Créer un modèle excel')]]")
            ));
            createExcelButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-dialog-create-model")));

            // 1. Fill "Nom"
            WebElement nomInput = driver.findElement(By.cssSelector("input[formcontrolname='nomCtrl']"));
            nomInput.sendKeys("Rapport Selenium");

            // 2. Choose "Fréquence" = QUOTIDIEN
            WebElement frequenceSelectElement = driver.findElement(By.cssSelector("select[formcontrolname='frequenceCtrl']"));
            Select frequenceSelect = new Select(frequenceSelectElement);
            frequenceSelect.selectByValue("QUOTIDIEN"); // or use .selectByVisibleText("Quotidienne")

            // 3. Skip Sites

            // 4. Groupe → Press Enter
            WebElement groupeSelect = driver.findElement(By.cssSelector("ng-select[formcontrolname='groupeCtrl'] input"));
            groupeSelect.click();
            groupeSelect.sendKeys(Keys.ENTER);

            // 5. Skip Dossier

            // 6. Fill Description
            WebElement descInput = driver.findElement(By.cssSelector("textarea[formcontrolname='descriptionCtrl']"));
            descInput.sendKeys("Ceci est un test automatisé.");

            // 7. Upload File
            URL resource = getClass().getClassLoader().getResource("rapport.xlsx");
            if (resource == null) {
                throw new RuntimeException("Fichier rapport.xlsx introuvable dans les resources.");
            }
            File fileToUpload = new File(resource.toURI());
            System.out.println("Uploading file: " + fileToUpload.getAbsolutePath());

            WebElement fileInput = driver.findElement(By.id("inputFile"));
            fileInput.sendKeys(fileToUpload.getAbsolutePath());

            // 8. Wait for "Enregistrer" to be clickable
            WebElement enregistrerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Enregistrer') and not(@disabled)]")));
            enregistrerBtn.click();


            Thread.sleep(500); // let modal animate
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.modal-content")));
            System.out.println("DEBUG: Modal closed");
            // 9. Screenshot
            takeScreenshot(driver, "modele excel crée");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
