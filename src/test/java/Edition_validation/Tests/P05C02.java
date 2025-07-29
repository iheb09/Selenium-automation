package Edition_validation.Tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
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
//            WebElement creerDossierBtn = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//button[@title='Créer un dossier' and .//span[contains(text(), 'Créer un dossier')]]")
//            ));
//            creerDossierBtn.click();
//            takeScreenshot(driver,"2","Creer_dossier_modal");
//            // 1. Press on the first combobox ("Type de dossier à créer")
//            WebElement folderTypeCombo = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//select[@formcontrolname='folderTypeCtrl']")
//            ));
//            folderTypeCombo.click(); // Open dropdown
//            folderTypeCombo.sendKeys(Keys.ARROW_DOWN);
//            folderTypeCombo.sendKeys(Keys.ARROW_DOWN);
//            folderTypeCombo.sendKeys(Keys.ENTER);
//
//            // 2. Fill in the "Nom" field
//            WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//input[@formcontrolname='nameFolderCtrl']")
//            ));
//            nameInput.sendKeys("Test_iheb_edition_validation");
//
//            // 3. Press on the second combobox ("Site"), then press Enter
//            WebElement siteComboInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//ng-select[@formcontrolname='siteCtrl']//input[@type='text']")
//            ));
//            siteComboInput.click(); // Focus input
//            Thread.sleep(500); // allow dropdown to load
//            siteComboInput.sendKeys(Keys.ENTER);
//
//            WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//button[@type='submit' and contains(., 'Créer') and not(@disabled)]")
//            ));
//            createButton.click();
//            Thread.sleep(1000);
            By folderLocator = By.xpath("//div[contains(@class, 'folder') and contains(normalize-space(), 'Test_iheb_edition_validation')]");
            WebElement folder = wait.until(ExpectedConditions.presenceOfElementLocated(folderLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", folder);
            wait.until(ExpectedConditions.visibilityOf(folder));
            Thread.sleep(500);
            Actions actions = new Actions(driver);
            actions.moveToElement(folder).contextClick().perform();

            takeScreenshot(driver,"3","Creer_dossier_done");
            By ajouterVariables = By.xpath("//ul[contains(@class, 'dropdown-menu') and contains(@class, 'show')]//a[contains(., 'Ajouter des variables')]");
            WebElement ajouterBtn = wait.until(ExpectedConditions.elementToBeClickable(ajouterVariables));
            ajouterBtn.click();
            takeScreenshot(driver,"4","ajouter_variables_clicked");

            By checkIcon = By.cssSelector("div.slick-cell.l0.r0.boutonsAction.true i.fa-check");
            WebElement iconElement = wait.until(ExpectedConditions.presenceOfElementLocated(checkIcon));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", iconElement);
            wait.until(ExpectedConditions.elementToBeClickable(iconElement)).click();

            Thread.sleep(500);

            By validerBtn = By.xpath("//div[contains(@class, 'modal-footer')]//button[contains(., 'Valider') and not(@disabled)]");
            WebElement validerElement = wait.until(ExpectedConditions.elementToBeClickable(validerBtn));
            validerElement.click();
            Thread.sleep(500);
            takeScreenshot(driver,"5","variable_added");





        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}