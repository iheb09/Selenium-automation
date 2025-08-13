package GestMetadonnees.Tests;

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

public class P01C03 {

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

            WebElement metadataCard = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'card-body') and .//h5[text()='Métadonnées']]")
            ));
            metadataCard.click();

            WebElement monositeTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), 'Monosite') and contains(@class, 'tabstyle')]")
            ));
            monositeTab.click();

            WebElement addMetadataButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(text(), \"Ajout d'une métadonnée\")]]")
            ));
            addMetadataButton.click();

            // Fill in "Code"
            WebElement codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[formcontrolname='codeCtrl']")
            ));
            codeInput.sendKeys("TST_iheb");

            // Fill in "Première valeur"
            WebElement valeurInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[formcontrolname='firstValueCtrl']")
            ));
            valeurInput.sendKeys("1");

            // Wait for "Ajouter" button to be enabled
            WebElement ajouterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Ajouter') and not(@disabled)]")
            ));
            ajouterButton.click();
            Thread.sleep(2000);
            takeScreenshot(driver,"2","Metadonne_added");

            Actions actions = new Actions(driver);
            WebElement tst_Element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'meta-node') and .//span[text()='TST_iheb']]")
            ));
            tst_Element.click();
            WebElement targetDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'meta-node') and contains(@class,'variable')]//span[text()='1']/parent::div")
            ));
            actions.contextClick(targetDiv).perform();
            WebElement renommerOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(.,'Renommer') and contains(@class,'hasSubMenu')]")
            ));
            renommerOption.click();
            WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Ok']/ancestor::span//input[@type='text']")
            ));
            inputField.click();
            inputField.clear();
            inputField.sendKeys("edited");
            takeScreenshot(driver,"3","valeur");
            WebElement okBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Ok']")
            ));
            okBtn.click();
            Thread.sleep(1000);
            takeScreenshot(driver,"4","valeur_modifiée");
            WebElement addSubMetaBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[normalize-space()='Ajouter une sous-métadonnée']")
            ));
            addSubMetaBtn.click();
            WebElement codeField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(.,'Code de la sous-métadonnée')]/following::input[1]")
            ));
            codeField.click();
            codeField.clear();
            codeField.sendKeys("SUB_META_CODE");
            WebElement firstValueField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(.,'Saisissez une première valeur')]/following::input[1]")
            ));
            firstValueField.click();
            firstValueField.clear();
            firstValueField.sendKeys("Valeur");
            takeScreenshot(driver,"5","Sous_metadonnée_avant");
            WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Créer' and not(@disabled)]")
            ));
            createBtn.click();
            takeScreenshot(driver,"6","Sous_metadonnée_aprés");

            WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='dSearchVar']//input[@placeholder='Rechercher']")
            ));
            searchField.click();

            WebElement tstElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'meta-node') and .//span[text()='TST_iheb']]")
            ));
            actions.contextClick(tstElement).perform();
            WebElement ajouterValeur = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(., 'Ajouter une valeur')]")
            ));
            ajouterValeur.click();
            WebElement valeurInput2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[placeholder='Entrer une valeur monosite']")
            ));
            valeurInput2.click();
            valeurInput2.sendKeys("2");
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()=' Ok ']")
            ));
            okButton.click();
            takeScreenshot(driver,"7","Metadonne_valeur_added");
            WebElement addedValue = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'meta-node')]//span[text()='2']")
            ));
            actions.contextClick(addedValue).perform();
            WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(.,'Supprimer')]")
            ));
            deleteOption.click();
            WebElement confirmDelete = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Confirmer')]")
            ));
            confirmDelete.click();
            takeScreenshot(driver,"8","Metadonne_valeur_deleted");

            WebElement tstElement2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'meta-node') and .//span[text()='TST_iheb']]")
            ));
            actions.contextClick(tstElement2).perform();
            WebElement renameLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(., 'Renommer la métadonnée')]")
            ));
            renameLink.click();
            WebElement input_Field = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'dropdown') and contains(@class, 'open') and contains(@class, 'show')]//input[@type='text' and contains(@class, 'form-control')]")
            ));
            input_Field.sendKeys("TST_iheb_edited");

            WebElement okButton2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Ok']")
            ));
            okButton2.click();
            Thread.sleep(500);
            takeScreenshot(driver,"9","Metadonne_renamed");
            WebElement deleteLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[.//i[contains(@class, 'fa-trash')] and contains(., 'Supprimer une métadonnée')]")
            ));

            deleteLink.click();
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'btn-success') and normalize-space(text())='Confirmer']")
            ));
            confirmButton.click();


            takeScreenshot(driver,"10","Metadonne_deleted");


        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}