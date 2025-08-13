package Support_Fractions.Tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
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

public class P01C06 {

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

            WebElement referentielDiv = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'btnDashboard') and .//label[text()='Référentiel']]")
            ));
            referentielDiv.click();

            WebElement supportsCard = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'card-body') and .//h5[text()='Supports']]")
            ));
            supportsCard.click();

//            WebElement creerSupportBtn = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//button[@id='btn.id' and .//span[normalize-space()='Créer un support']]")
//            ));
//            creerSupportBtn.click();
//
//            // Code
//            WebElement codeField = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("input[formcontrolname='codeCtrl']")
//            ));
//            codeField.sendKeys("100");
//
//            // Libellé
//            WebElement libelleField = driver.findElement(
//                    By.cssSelector("input[formcontrolname='libelleCtrl']")
//            );
//            libelleField.sendKeys("Iheb Test");
//
//            // Nom International
//            WebElement nomInterField = driver.findElement(
//                    By.cssSelector("input[formcontrolname='nomInternaCtrl']")
//            );
//            nomInterField.sendKeys("Iheb International Test");
//
//            // Statut dropdown
//            WebElement statutSelect = driver.findElement(
//                    By.cssSelector("select[formcontrolname='statutCtrl']")
//            );
//            Select statutDropdown = new Select(statutSelect);
//            statutDropdown.selectByVisibleText("Validé");
//
//            // Auteur
//            WebElement auteurField = driver.findElement(
//                    By.cssSelector("input[formcontrolname='auteurCtrl']")
//            );
//            auteurField.sendKeys("Iheb");
//
//            // Commentaire
//            WebElement commentaireArea = driver.findElement(
//                    By.cssSelector("textarea[formcontrolname='commentaireCtrl']")
//            );
//            commentaireArea.sendKeys("Ceci est un commentaire test.");
//
//            takeScreenshot(driver,"2","Form_creaate_support_filled");
//
//            // Attendre que le bouton Ajouter soit activé
//            WebElement ajouterButton = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//button[normalize-space()='Ajouter']")
//            ));
//            ajouterButton.click();
//            Thread.sleep(1000);
//            takeScreenshot(driver,"3","support_created");

            WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input.form-control.search-filter.filter-code_sandre")
            ));
            searchField.clear();
            searchField.sendKeys("100");
            Thread.sleep(2000);
            takeScreenshot(driver,"4","support_located");
            WebElement voirFractionsBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a.btn.btn-sm.btn-primary.btn-ouvrir")
            ));
            voirFractionsBtn.click();
            Thread.sleep(1000);
            takeScreenshot(driver,"5","accessing_fractions");
            WebElement creerFractionBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(text(),'Créer une fraction')]]")
            ));
            creerFractionBtn.click();

            // Fill "Code"
            WebElement codeInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[formcontrolname='codeCtrl']")
            ));
            codeInput.sendKeys("1000");

            // Fill "Libellé"
            WebElement libelleInput = driver.findElement(By.cssSelector("input[formcontrolname='libelleCtrl']"));
            libelleInput.sendKeys("Fraction test iheb");

            // Fill "Nom international"
            WebElement nomIntInput = driver.findElement(By.cssSelector("input[formcontrolname='internationalNameCtrl']"));
            nomIntInput.sendKeys("International Fraction iheb");

            // Fill "Auteur"
            WebElement auteurInput = driver.findElement(By.cssSelector("input[formcontrolname='authorCtrl']"));
            auteurInput.sendKeys("iheb");

            // Select "Statut"
            WebElement statutSelect = driver.findElement(By.cssSelector("select[formcontrolname='statutCtrl']"));
            Select statutDropdown = new Select(statutSelect);
            statutDropdown.selectByVisibleText("Validé");

            // Fill "Commentaire"
            WebElement commentaireInput = driver.findElement(By.cssSelector("textarea[formcontrolname='commentaireCtrl']"));
            commentaireInput.sendKeys("Ceci est un commentaire de test.");
            takeScreenshot(driver,"6","fraction_filled");

            // Click "Ajouter"
            WebElement ajouterBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Ajouter']")
            ));
            ajouterBtn.click();

            WebElement searchField2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input.form-control.search-filter.filter-code_sandre")
            ));
            searchField2.clear();
            searchField2.sendKeys("1000");
            Thread.sleep(1000);
            takeScreenshot(driver,"7","fraction_added");














        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}