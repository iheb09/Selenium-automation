package GestVariables.Tests;

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

public class P01C01 {

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
            WebElement gestionVariablesCard = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h5[normalize-space()='Gestion des variables']/parent::div[@class='card-body']")
            ));
            gestionVariablesCard.click();
            WebElement variablesGroupesCard = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h5[normalize-space()='Variables par groupes']/parent::div[@class='card-body']")
            ));
            variablesGroupesCard.click();
            WebElement voirButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.slick-cell.l1.r1.boutonsAction.true button.btn.btn-sm.btn-ouvrir[title='Voir']")
            ));
            voirButton.click();
            WebElement creerVariableBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Créer une variable' and span[text()=' Créer une variable']]")
            ));
            creerVariableBtn.click();

            WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Sélectionner')]")
            ));
            selectButton.click();
            // Click to open the ng-select dropdown
            WebElement typeVariableSelect = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("ng-select#sTypeVariable")
            ));
            typeVariableSelect.click();
            // Send ENTER to whatever currently has focus
            driver.switchTo().activeElement().sendKeys(Keys.ENTER);

            // Open the dropdown
            WebElement dropdownInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".ng-value-container .ng-input input")
            ));
            dropdownInput.click();
            // Click on the exact ng-select dropdown
            WebElement exactNgSelect = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("ng-select.ng-select-single.ng-select-typeahead.ng-select-taggable.ng-select-searchable.ng-select-clearable")
            ));
            exactNgSelect.click();
            // Press Enter to select the highlighted option
            driver.switchTo().activeElement().sendKeys(Keys.ENTER);
            // 1. Type "tst_iheb" in inputCodeVariable
            WebElement codeVariableInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("inputCodeVariable")
            ));
            codeVariableInput.sendKeys("tst_iheb");

// 2. Click sTypeDonnees and press ENTER
            WebElement typeDonneesSelect = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("ng-select#sTypeDonnees")
            ));
            typeDonneesSelect.click();
            driver.switchTo().activeElement().sendKeys(Keys.ENTER);

// 3. Type "proprites generales test" in textareaDescription
            WebElement descriptionTextarea = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("textareaDescription")
            ));
            descriptionTextarea.sendKeys("proprites generales test");

            // 1️⃣ Click on the input to open the calendar
            WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("ng2-flatpickr input.flatpickr-input")
            ));
            dateInput.click();

// 2️⃣ Wait for the calendar to appear
            WebElement calendar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".flatpickr-calendar")
            ));
// 4️⃣ Click on the day 14
            WebElement day14 = calendar.findElement(By.xpath("//span[contains(@class,'flatpickr-day') and text()='14' and not(contains(@class,'prevMonthDay')) and not(contains(@class,'nextMonthDay'))]"));            day14.click();
            takeScreenshot(driver,"2","Creation of variables");
            WebElement suivantBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()=' Suivant ']")
            ));
            suivantBtn.click();

            takeScreenshot(driver,"3","Suivant");

            /// First select (Tst_ihebtst_iheb_edited)
            WebElement selectElement1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Tst_ihebtst_iheb_edited']/following-sibling::select")
            ));

            // Use Selenium's Select helper
            Select dropdown = new Select(selectElement1);
            dropdown.selectByVisibleText("edited"); // or dropdown.selectByValue("5238")

// Second select (Sub_meta_code)
            WebElement secondSelect = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[normalize-space()='Sub_meta_code']/following-sibling::select")
            ));
            Select dropdown2 = new Select(secondSelect);
            dropdown2.selectByVisibleText("Valeur");
            // Locate "Suivant" button directly
            WebElement suivantBtn2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@awnextstep and normalize-space(text())='Suivant']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", suivantBtn2);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", suivantBtn2);

            takeScreenshot(driver,"4","Suivant");
            // Wait for the "Enregistrer" button to be present and visible
            WebElement enregistrerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("btnEnregistrer")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", enregistrerBtn);
            Thread.sleep(500);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(enregistrerBtn)).click();
            } catch (Exception e) {
                // Fallback to JS click if Selenium fails (covered element / overlay issue)
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", enregistrerBtn);
            }
            Thread.sleep(2000);
            takeScreenshot(driver,"5","Enregistrer");





        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}