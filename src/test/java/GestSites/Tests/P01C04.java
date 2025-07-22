package GestSites.Tests;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            codeInput.sendKeys("test_iheb2");

            // 2. Fill "Nom *"
            WebElement nomInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[formcontrolname='nomCtrl']")
            ));
            nomInput.sendKeys("test_iheb2");

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

            takeScreenshot(driver,"debug","deb");
            // 5. Click on "Groupe d'utilisateurs" input then press Enter
            WebElement groupeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("app-select[formcontrolname='groupesCtrl'] input[role='combobox']")  // inside ng-select
            ));
            groupeInput.click();
            Thread.sleep(500); // small delay to allow options to load
            groupeInput.sendKeys(Keys.ENTER);
            takeScreenshot(driver,"debug","debug");

            // 6. Click on "Ajouter" button
            WebElement ajouterBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Ajouter') and not(@disabled)]")
            ));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", ajouterBtn);
            ajouterBtn.click();
            System.out.println("✅ Site creation form submitted.");
            Thread.sleep(1000);
            takeScreenshot(driver,"3","site_crée");
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
