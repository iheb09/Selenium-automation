package GestSites.Tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class P01C04 {

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
    public void runTestVarGrp() throws Exception {
        WebDriver driver = null;

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            driver.get("https://dev.aquedi.fr/connexion");
            System.out.println("Running Test variable grp");


            Properties creds = loadCredentials();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iLogin")))
                    .sendKeys(creds.getProperty("username"));
            driver.findElement(By.id("iPassword"))
                    .sendKeys(creds.getProperty("password"));
            driver.findElement(By.id("btnConnexion")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Tableau de bord']")));


            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'btnDashboard') and .//label[text()='Référentiel']]"))).click();


            WebElement sitesCard = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'card-body') and .//h5[normalize-space()='Sites']]")
            ));
            sitesCard.click();
            takeScreenshot(driver,"1","access");

            WebElement creerSiteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space()='Créer un site']]")
            ));
            creerSiteBtn.click();
            takeScreenshot(driver,"2","creer_modal_open");
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 1. Fill "Code *"
            WebElement codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[formcontrolname='codeCtrl']")
            ));
            codeInput.sendKeys("test_iheb5");

            // 2. Fill "Nom *"
            WebElement nomInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[formcontrolname='nomCtrl']")
            ));
            nomInput.sendKeys("test_iheb5");

            // 3. Fill "Description"
            WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[formcontrolname='descriptionCtrl']")
            ));
            descriptionInput.sendKeys("Description de test");

            // 4. Click on "Heure début" input then press Enter
            WebElement heureDebutInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("ng2-flatpickr input.flatpickr-input")
            ));
            heureDebutInput.click();
            heureDebutInput.sendKeys("17");

            // 5. Click on "Groupe d'utilisateurs" input then press Enter
            WebElement groupeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("app-select[formcontrolname='groupesCtrl'] input[role='combobox']")  // inside ng-select
            ));
            groupeInput.click();
            Thread.sleep(500); // small delay to allow options to load
            groupeInput.sendKeys(Keys.ENTER);

            // 6. Click on "Ajouter" button
            WebElement ajouterBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Ajouter') and not(@disabled)]")
            ));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", ajouterBtn);
            ajouterBtn.click();
            System.out.println("✅ Site creation form submitted.");
            Thread.sleep(1000);
            takeScreenshot(driver,"3","site_crée");



            By listeOuvragesButton = By.xpath("(//button[contains(., 'Liste des ouvrages')])[2]");
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(listeOuvragesButton));
            button.click();

            By creerOuvrageButton = By.xpath("//button[.//span[contains(text(), 'Créer un ouvrage')]]");
            WebElement buttonouvrage = wait.until(ExpectedConditions.elementToBeClickable(creerOuvrageButton));
            buttonouvrage.click();

            // Fill "Nom"
            WebElement nomInput2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='nomCtrl']")));
            nomInput2.sendKeys("Test_iheb2");

// Fill "Code Sandre"
            WebElement codeSandreInput = driver.findElement(By.cssSelector("input[formcontrolname='codeSandreCtrl']"));
            codeSandreInput.sendKeys("TST_IHEB3");

// Click & select in "Departement"
            WebElement departement = driver.findElement(By.cssSelector("ng-select[formcontrolname='departementCtrl'] input[type='text']"));
            departement.click();
            departement.sendKeys(Keys.ENTER);
            Thread.sleep(1000);

// Wait for "Commune" to be enabled
            WebElement commune = driver.findElement(By.cssSelector("ng-select[formcontrolname='communeCtrl'] input[type='text']:not([disabled])"));
            commune.click();
            commune.sendKeys(Keys.ENTER);

// Click & select in "Maitre d'ouvrage"
            WebElement maitreOuvrage = driver.findElement(By.cssSelector("app-select[formcontrolname='maitreOuvrageCtrl'] ng-select input[type='text']"));
            maitreOuvrage.click();
            maitreOuvrage.sendKeys(Keys.ENTER);

// Click & select in "Exploitant"
            WebElement exploitant = driver.findElement(By.cssSelector("app-select[formcontrolname='exploitantCtrl'] ng-select input[type='text']"));
            exploitant.click();
            exploitant.sendKeys(Keys.ENTER);

// Fill "Commentaire"
            WebElement commentaireInput = driver.findElement(By.cssSelector("input[formcontrolname='commentaireCtrl']"));
            commentaireInput.sendKeys("commentaire_iheb1");



            List<WebElement> ajouterButtons = driver.findElements(By.xpath("//button[normalize-space(text())='Ajouter']"));

            boolean clicked = false;

            for (WebElement buttonA : ajouterButtons) {
                if (buttonA.isDisplayed() && buttonA.isEnabled()) {
                    try {
                        buttonA.click();
                        System.out.println("✅ Clicked on the 'Ajouter' button.");
                        clicked = true;
                        break;
                    } catch (Exception e) {
                        System.out.println("⛔ Failed to click a button: " + e.getMessage());
                    }
                }
            }

            if (!clicked) {
                throw new RuntimeException("❌ Could not find a clickable 'Ajouter' button.");
            }
            Thread.sleep(1000);
            takeScreenshot(driver,"4","ouvrage_added");

            WebElement ptdemesure = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Points de mesure')]")
            ));
            ptdemesure.click();
            Thread.sleep(500);

//            // Locate the button using the span text
//            By creerPointMesureBtn = By.xpath("//button[.//span[normalize-space(text())='Créer un point de mesure']]");
//            WebElement buttonPt = wait.until(ExpectedConditions.elementToBeClickable(creerPointMesureBtn));
//            buttonPt.click();
//            System.out.println("✅ Clicked on 'Créer un point de mesure' button.");
//
//
//            Actions actions = new Actions(driver);
//            // 1. Fill "Nom du point de mesure"
//            WebElement nomInput1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.xpath("//input[@formcontrolname='ptsMesureNomCtrl']")));
//            nomInput1.sendKeys("PM_TEST");
//
//// 2. Fill "Code du point de mesure"
//            WebElement codeInput1 = driver.findElement(By.xpath("//input[@formcontrolname='numPointMesureCtrl']"));
//            codeInput1.sendKeys("PM123");
//
//// 3. Click on "Support" combobox and press ENTER
//            WebElement supportCombo = driver.findElement(By.xpath("//ng-select[@formcontrolname='supportCtrl']//input"));
//            supportCombo.click();
//            actions.sendKeys(Keys.ENTER).perform();  // select first option
//
//// 4. Click on "Localisation du point de mesure" combobox and press ENTER
//            WebElement localisationCombo = driver.findElement(By.xpath("//ng-select[@formcontrolname='elementCtrl']//input"));
//            localisationCombo.click();
//            actions.sendKeys(Keys.ENTER).perform();  // select first option
//
//            List<WebElement> ajouterButtons1 = driver.findElements(By.xpath("//button[normalize-space(text())='Ajouter']"));
//
//            boolean clicked1 = false;
//
//            for (WebElement buttonA : ajouterButtons1) {
//                if (buttonA.isDisplayed() && buttonA.isEnabled()) {
//                    try {
//                        buttonA.click();
//                        System.out.println("✅ Clicked on the 'Ajouter' button.");
//                        clicked1 = true;
//                        break;
//                    } catch (Exception e) {
//                        System.out.println("⛔ Failed to click a button: " + e.getMessage());
//                    }
//                }
//            }
//
//            if (!clicked1) {
//                throw new RuntimeException("❌ Could not find a clickable 'Ajouter' button.");
//            }
//            Thread.sleep(1000);
//            takeScreenshot(driver,"5","pt_mesure_added");
//            System.out.println("✅ Modal form filled and submitted.");

            WebElement creerEnMasseBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(normalize-space(), 'Créer en masse des points de mesure')]]")
            ));
            creerEnMasseBtn.click();
            System.out.println("✅ Clicked 'Créer en masse des points de mesure' button.");

            // Step 1: Log working dir and resolve absolute path
            File fileToUpload = Paths.get("src", "test", "resources", "rapport.xlsx").toAbsolutePath().toFile();
            System.out.println("Working directory: " + new File(".").getAbsolutePath());
            System.out.println("Resolved file path: " + fileToUpload.getAbsolutePath());

            if (!fileToUpload.exists()) {
                throw new RuntimeException("❌ Fichier introuvable à l'emplacement : " + fileToUpload.getAbsolutePath());
            }

            // Step 2: Locate the input element
            WebElement fileInput = driver.findElement(By.id("inputFile"));

            // Step 3: If Remote, use FileDetector via RemoteWebElement
            if (fileInput instanceof RemoteWebElement) {
                System.out.println("Detected RemoteWebElement, applying LocalFileDetector.");
                ((RemoteWebElement) fileInput).setFileDetector(new LocalFileDetector());
                ((RemoteWebElement) fileInput).sendKeys(fileToUpload.getAbsolutePath());
            } else {
                System.out.println("Detected local WebDriver, sending file path directly.");
                fileInput.sendKeys(fileToUpload.getAbsolutePath());
            }

            // Step 4: Take screenshot for debug

            // Wait for the modal and "Importer" button to be present and clickable
            WebElement importerButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'modal-content')]//button[contains(@class, 'btn-primary') and normalize-space(text())='Importer']")
            ));
            importerButton.click();
            Thread.sleep(500);
            takeScreenshot(driver,"5","pt_mesure_enmasse");




        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}