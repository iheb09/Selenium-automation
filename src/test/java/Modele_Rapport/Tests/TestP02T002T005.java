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

public class TestP02T002T005 {

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
    public void runTestP02T002() throws Exception {
        WebDriver driver = null;

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);

            // Enable remote file uploads
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());

            // Set full screen resolution
            driver.manage().window().setSize(new Dimension(1920, 1080));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            System.out.println("Running TestP02T002-T005");

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
                    By.xpath("//button[.//span[contains(normalize-space(), 'Créer un modèle de rapport dans Aquedi')]]")
            ));
            createExcelButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-dialog-create-model")));

            WebElement nomInput = driver.findElement(By.cssSelector("input[formcontrolname='nomCtrl']"));
            nomInput.sendKeys("Rapport Selenium");

            WebElement frequenceSelectElement = driver.findElement(By.cssSelector("select[formcontrolname='frequenceCtrl']"));
            Select frequenceSelect = new Select(frequenceSelectElement);
            frequenceSelect.selectByValue("QUOTIDIEN");

            WebElement groupeSelect = driver.findElement(By.cssSelector("ng-select[formcontrolname='groupeCtrl'] input"));
            groupeSelect.click();
            groupeSelect.sendKeys(Keys.ENTER);

            WebElement descInput = driver.findElement(By.cssSelector("textarea[formcontrolname='descriptionCtrl']"));
            descInput.sendKeys("test 1.1 scenario t002-t003.");

            WebElement enregistrerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Enregistrer') and not(@disabled)]")));
            enregistrerBtn.click();

            Thread.sleep(500);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.modal-content")));
            System.out.println("DEBUG: Modal closed");

            takeScreenshot(driver, "modele aquedi crée");

            WebElement sourceVariable = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class, 'variableLigne')]//span[@title='AA1ASDIG_H2S']/ancestor::li")
            ));

            WebElement dropZone = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(),'Deposez des variables')]")
            ));

            new Actions(driver)
                    .moveToElement(sourceVariable)
                    .clickAndHold()
                    .pause(Duration.ofMillis(500))
                    .moveToElement(dropZone)
                    .pause(Duration.ofMillis(500))
                    .release()
                    .build()
                    .perform();

            WebElement newCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@title='AAA31DIG_DMDS']/ancestor::div[contains(@class,'d-flex')]//input[@type='checkbox']")
            ));
            newCheckbox.click();

            WebElement ajouterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.button-ajouter")
            ));
            ajouterButton.click();

            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");

            takeScreenshot(driver, "drag and drop , checkbox");


            WebElement ajouterOngletBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.nav-link.nav-link-add[title='Ajouter un nouvel onglet']")));
            ajouterOngletBtn.click();

            WebElement donnees2Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[contains(@class,'nav-item')]//div[contains(@class,'nav-link') and contains(., 'Données 1')]")));
            new Actions(driver).contextClick(donnees2Tab).perform();

            WebElement renommerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(., 'Renommer')]")));
            renommerBtn.click();

            // UPDATED RENAME CODE
            WebElement renameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("li.ng-star-inserted input.form-control[type='text']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", renameInput);
            renameInput.click();
            renameInput.sendKeys("Données modifiées");

            wait.until(driver1 -> renameInput.getAttribute("value").equals("Données modifiées"));

            WebElement okBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class,'ng-star-inserted')]//button[normalize-space()='Ok']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okBtn);

            takeScreenshot(driver, "renommer_donnees_input");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'nav-link')]/span[text()='Données modifiées']")));

            WebElement donnees1Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'nav-link') and contains(., 'Données 2')]")));
            new Actions(driver).contextClick(donnees1Tab).perform();

            WebElement supprimerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(., 'Supprimer')]")));
            supprimerBtn.click();

            // Wait for the confirmation modal to appear and click "Confirmer"
            WebElement confirmerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space()='Confirmer']")
            ));
            confirmerBtn.click();

            WebElement enregistrerDonneesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@id='btn.id' and .//span[normalize-space()='Enregistrer']]")
            ));
            enregistrerDonneesBtn.click();



            Thread.sleep(1000);
            takeScreenshot(driver, "after editing 1ST tab and deleting 2ND");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
