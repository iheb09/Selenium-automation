package GestBilanSandre.Tests;

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
import java.util.List;
import java.util.Properties;

public class TestP04 {

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
            System.out.println("Running Test P04");

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
//            fermerBtn1.click();
            takeScreenshot(driver,"1","Accessing_site");

            WebElement bilansSandreBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'btnDashboard')]//label[normalize-space()='Bilans Sandre']/parent::div")
            ));
            bilansSandreBtn.click();
            WebElement nodeToDoubleClick = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'node-content-wrapper') and .//span[normalize-space()='test_iheb']]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick);
            new Actions(driver).doubleClick(nodeToDoubleClick).perform();
            Thread.sleep(2000);
            takeScreenshot(driver,"2","Accesing_Gest_Blian_Sandre");

//            // Click "Créer un bilan" button
//            WebElement creerBilanBtn = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//span[contains(text(), 'Créer un bilan')]/ancestor::button")));
//            creerBilanBtn.click();
//
//            // Wait for the "Nom" field to appear (ensures modal is loaded)
//            WebElement nomInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sNom")));
//            takeScreenshot(driver, "3", "Form_open");
//
//            // Fill "Nom"
//            nomInput.sendKeys("Nom de Test");
//
//            // Fill "Description"
//            WebElement descInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sDescription")));
//            descInput.sendKeys("Ceci est une description de test");
//
//            // --- SITES ---
//            WebElement siteInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("ng-select[formcontrolname='siteCtrl'] input")));
//            siteInput.click();
//            Thread.sleep(500); // optional
//            siteInput.sendKeys(Keys.ENTER);
//
//            WebElement groupeComboBox = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("ng-select[formcontrolname='groupeBilanSandreCtrl'] input[role='combobox']")
//            ));
//            groupeComboBox.click(); // To open the dropdown
//            Thread.sleep(500);
//            groupeComboBox.sendKeys(Keys.ENTER); // To select the first option
//
//            // --- ÉMETTEUR ---
//            WebElement emetteurInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("app-select-optimzed[formcontrolname='emetteurCtrl'] input")));
//            emetteurInput.click();
//            Thread.sleep(500);
//            emetteurInput.sendKeys(Keys.ENTER);
//
//            // --- DESTINATAIRE ---
//            WebElement destinataireInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("app-select-optimzed[formcontrolname='destinataireCtrl'] input")));
//            destinataireInput.click();
//            Thread.sleep(500);
//            destinataireInput.sendKeys(Keys.ARROW_DOWN);
//            destinataireInput.sendKeys(Keys.ENTER);
//            WebElement suivantBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//button[@awnextstep and contains(text(), 'Suivant')]")
//            ));
//            ((JavascriptExecutor) driver).executeScript(
//                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
//                    suivantBtn
//            );
//            Thread.sleep(500);
//            takeScreenshot(driver, "4", "First_step_filled");
//            WebElement suivantButton = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("button[awnextstep].btn.tbn-default.ml-3:not([disabled])")
//            ));
//            suivantButton.click();
//            Thread.sleep(1000);
//            takeScreenshot(driver, "5", "Second_step_initiated");
//
//            // 1. Click the checkbox input inside the div with class 'mat-checkbox-inner-container'
//            WebElement checkboxContainer = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("div.mat-checkbox-inner-container.mat-checkbox-inner-container-no-side-margin")
//            ));
//            Thread.sleep(500
//            );
//            checkboxContainer.click();
//
//            // 2. Scroll to the "Ajouter" button and click it (wait until enabled)
//            WebElement ajouterBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.cssSelector("button.btn.btn-primary.px-5.py-2")
//            ));
//
//            // Scroll the "Ajouter" button into view using JavaScript
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", ajouterBtn);
//
//            // Wait until the button is enabled (not disabled attribute)
//            wait.until(d -> ajouterBtn.isEnabled());
//            ajouterBtn.click();
//
//            // 3. Click the "Suivant" button
//            WebElement suivantBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("button.btn.btn-primary.ml-2[awnextstep]")
//            ));
//            suivantBtn2.click();
//            Thread.sleep(500);
//            takeScreenshot(driver, "6", "Second_step_filled");
//            Thread.sleep(1000);
//
//
//            //3 eme etape
//            // We'll use a loop to iterate all visible td elements and click the one matching text exactly
//            List<WebElement> possibleCells = driver.findElements(By.xpath("//td[contains(text(), 'BFA21_MS2')]"));
//            boolean clicked = false;
//            for (WebElement cell : possibleCells) {
//                String text = cell.getText().trim();
//                System.out.println("🔍 Checking cell: '" + text + "'");
//
//                if (text.equals("BFA21_MS2")) {
//                    System.out.println("✅ Match found. Scrolling into view...");
//                    ((JavascriptExecutor) driver).executeScript(
//                            "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", cell);
//                    Thread.sleep(300);
//
//                    // Wait again for visibility + clickability
//                    wait.until(ExpectedConditions.visibilityOf(cell));
//                    wait.until(ExpectedConditions.elementToBeClickable(cell));
//
//                    // Use Actions for reliability
//                    new Actions(driver).moveToElement(cell).click().perform();
//                    System.out.println("🎯 Clicked cell with text: BFA21_MS2");
//                    clicked = true;
//                    break;
//                }
//            }
//            if (!clicked) {
//                throw new NoSuchElementException("❌ Could not find or click BFA21_MS2 among visible cells.");
//            }
//            // Scroll to and click the "Destination" combo box input
//            WebElement destinationComboBox = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("ng-select#destination input[role='combobox']")
//            ));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", destinationComboBox);
//            Thread.sleep(300); // let dropdown render
//            destinationComboBox.click();
//            destinationComboBox.sendKeys(Keys.ENTER);
//
//            List<WebElement> ajouterButtons = driver.findElements(By.xpath("//button[contains(., 'Ajouter')]"));
//            System.out.println("🔢 Found " + ajouterButtons.size() + " 'Ajouter' buttons.");
//
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            boolean clicked1 = false;
//
//            for (int i = 0; i < ajouterButtons.size(); i++) {
//                WebElement btn = ajouterButtons.get(i);
//                System.out.println("➡️ Checking button #" + (i + 1));
//
//                if (btn.isDisplayed()) {
//                    // Scroll to this button
//                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
//                    Thread.sleep(1000);
//
//                    // Check if clickable
//                    try {
//                        wait.until(ExpectedConditions.elementToBeClickable(btn));
//                        System.out.println("✅ Button #" + (i + 1) + " is clickable. Clicking...");
//                        btn.click();
//                        clicked1 = true;
//                        break; // stop after first clickable click
//                    } catch (TimeoutException e) {
//                        System.out.println("❌ Button #" + (i + 1) + " not clickable yet.");
//                    }
//                } else {
//                    System.out.println("⛔ Button #" + (i + 1) + " is not displayed.");
//                }
//            }
//
//            if (!clicked1) {
//                throw new RuntimeException("❌ No visible and clickable 'Ajouter' button found!");
//            }
//
//
//
//
//
//
//
//
//
//
//
//
//            List<WebElement> suivantButtons = driver.findElements(By.xpath("//button[contains(text(), 'Suivant')]"));
//            System.out.println("🔢 Found " + suivantButtons.size() + " 'Suivant' buttons.");
//
//            boolean clicked3 = false;
//
//            for (int i = 0; i < suivantButtons.size(); i++) {
//                WebElement btn = suivantButtons.get(i);
//
//                String classes = btn.getAttribute("class");
//                String awnextstep = btn.getAttribute("awnextstep");
//
//                System.out.println("➡️ Checking button #" + (i + 1) + " with class='" + classes + "' and awnextstep='" + awnextstep + "'");
//                System.out.println("Displayed: " + btn.isDisplayed() + ", Enabled: " + btn.isEnabled() +
//                        ", Disabled attr: " + btn.getAttribute("disabled") + ", aria-disabled: " + btn.getAttribute("aria-disabled"));
//
//                if ("btn btn-primary ml-2".equals(classes) && awnextstep != null) {
//                    js.executeScript(
//                            "var rect = arguments[0].getBoundingClientRect();" +
//                                    "window.scrollBy(0, rect.top - window.innerHeight / 2);", btn);
//                    Thread.sleep(300);
//
//                    try {
//                        wait.until(ExpectedConditions.elementToBeClickable(btn));
//                        btn.click();
//                        System.out.println("✅ Clicked button #" + (i + 1));
//                    } catch (TimeoutException e) {
//                        System.out.println("Clickable wait failed, clicking via JavaScript instead");
//                        js.executeScript("arguments[0].click();", btn);
//                    }
//
//                    clicked3 = true;
//                    break;
//                }
//            }
//
//            if (!clicked3) {
//                throw new RuntimeException("❌ Could not find or click the exact 'Suivant' button.");
//            }
//
//            takeScreenshot(driver, "7", "third_step");
//
//            //ETAPE 4
//
//            // 1. Click on definitionVariableCtrl (ng-select input) and press ENTER
//            WebElement defVarInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("ng-select#sDefinitionVariable input[role='combobox']")
//            ));
//            defVarInput.click();
//            Thread.sleep(300);
//            defVarInput.sendKeys(Keys.ENTER);
//
//
//            // 2. Click on nomCtrl input and send keys "test_iheb"
//            WebElement nomInput1 = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("input#edtNomVariable1[formcontrolname='nomCtrl']")
//            ));
//            nomInput1.click();
//            nomInput1.clear();
//            nomInput1.sendKeys("test_iheb");
//
//
//            // 3. Click on ouvrageCtrl (ng-select input inside app-select) and press ENTER
//            WebElement ouvrageInput = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("app-select#sOuvrage ng-select input[role='combobox']")
//            ));
//            ouvrageInput.click();
//            Thread.sleep(300);
//            ouvrageInput.sendKeys(Keys.ENTER);
//            Thread.sleep(300);
//
//            takeScreenshot(driver, "8", "4th_step_filled");
//
//// 5. Click the exact "Ajouter" button with class "btn btn-primary px-5"
//            // 1. Find all buttons containing "Ajouter"
//            List<WebElement> ajouterButtons2 = driver.findElements(
//                    By.xpath("//button[contains(@class, 'btn-primary') and contains(normalize-space(.), 'Ajouter')]")
//            );
//
//// 2. Try each one until we find a visible and enabled button
//            boolean clicked4 = false;
//            for (WebElement btn : ajouterButtons2) {
//                try {
//                    // Make sure it's displayed and enabled
//                    if (btn.isDisplayed() && btn.isEnabled()) {
//                        // Scroll into view
//                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
//                        Thread.sleep(300); // Let scroll settle
//
//                        // Wait until it's clickable
//                        wait.until(ExpectedConditions.elementToBeClickable(btn));
//
//                        // Click it
//                        btn.click();
//                        System.out.println("✅ Clicked visible and enabled 'Ajouter' button.");
//                        clicked4 = true;
//                        break;
//                    }
//                } catch (Exception e) {
//                    System.out.println("⚠️ Skipping a button due to error: " + e.getMessage());
//                }
//            }
//
//            if (!clicked4) {
//                System.out.println("❌ No visible and clickable 'Ajouter' button found.");
//            }
//            // 1. Find all buttons with text "Suivant"
//            List<WebElement> nextBtnCandidates = driver.findElements(
//                    By.xpath("//button[contains(normalize-space(.), 'Suivant')]")
//            );
//
//// 2. Try each one until we find a visible and enabled one
//            boolean nextClicked = false;
//            for (WebElement nextBtnCandidate : nextBtnCandidates) {
//                try {
//                    if (nextBtnCandidate.isDisplayed() && nextBtnCandidate.isEnabled()) {
//                        // Scroll into view
//                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nextBtnCandidate);
//                        Thread.sleep(300); // allow scroll to finish
//
//                        // Wait until clickable
//                        wait.until(ExpectedConditions.elementToBeClickable(nextBtnCandidate));
//
//                        // Click
//                        nextBtnCandidate.click();
//                        System.out.println("✅ Clicked visible and enabled 'Suivant' button.");
//                        nextClicked = true;
//                        break;
//                    }
//                } catch (Exception e) {
//                    System.out.println("⚠️ Skipping a 'Suivant' button due to error: " + e.getMessage());
//                }
//            }
//
//            if (!nextClicked) {
//                System.out.println("❌ No visible and clickable 'Suivant' button found.");
//            }
//
//            takeScreenshot(driver, "9", "5th_step_initiated");
//
//            // 1. Select the first visible + enabled "Définition de variable"
//            List<WebElement> defVarInputs = driver.findElements(
//                    By.cssSelector("ng-select[formcontrolname='definitionVariableCtrl'] div.ng-input input")
//            );
//
//            boolean defVarSelected = false;
//            for (WebElement input : defVarInputs) {
//                if (input.isDisplayed() && input.isEnabled()) {
//                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);
//                    wait.until(ExpectedConditions.elementToBeClickable(input));
//                    input.click();
//                    input.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
//                    System.out.println("✅ Selected 'Définition de variable'.");
//                    defVarSelected = true;
//                    break;
//                }
//            }
//            if (!defVarSelected) System.out.println("❌ No visible 'Définition de variable' input found.");
//
//// 2. Select the first visible + enabled "Ouvrage"
//            List<WebElement> ouvrageInputs = driver.findElements(
//                    By.cssSelector("app-select[formcontrolname='ouvrageCtrl'] ng-select div.ng-input input")
//            );
//
//            boolean ouvrageSelected = false;
//            for (WebElement input : ouvrageInputs) {
//                if (input.isDisplayed() && input.isEnabled()) {
//                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);
//                    wait.until(ExpectedConditions.elementToBeClickable(input));
//                    input.click();
//                    input.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
//                    System.out.println("✅ Selected 'Ouvrage'.");
//                    ouvrageSelected = true;
//                    break;
//                }
//            }
//            if (!ouvrageSelected) System.out.println("❌ No visible 'Ouvrage' input found.");
//
//// 3. Wait a bit for the form to react and enable the "Ajouter" button
//            Thread.sleep(800);
//
//// 4. Click the first visible + enabled "Ajouter" button
//            List<WebElement> ajouterButtons3 = driver.findElements(
//                    By.xpath("//button[contains(., 'Ajouter') and not(@disabled)]")
//            );
//
//            boolean ajouterClicked = false;
//            for (WebElement btn : ajouterButtons3) {
//                if (btn.isDisplayed() && btn.isEnabled()) {
//                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
//                    wait.until(ExpectedConditions.elementToBeClickable(btn));
//                    btn.click();
//                    System.out.println("✅ Clicked visible and enabled 'Ajouter' button.");
//                    ajouterClicked = true;
//                    break;
//                }
//            }
//
//            if (!ajouterClicked) {
//                System.out.println("❌ No visible and enabled 'Ajouter' button was found.");
//            }
//            takeScreenshot(driver, "10", "5th_step_filled");
//
//            // 1. Find all buttons with text "Suivant"
//            List<WebElement> nextBtnCandidates2 = driver.findElements(
//                    By.xpath("//button[contains(normalize-space(.), 'Suivant')]")
//            );
//
//// 2. Try each one until we find a visible and enabled one
//            boolean nextClicked2 = false;
//            for (WebElement nextBtnCandidate : nextBtnCandidates2) {
//                try {
//                    if (nextBtnCandidate.isDisplayed() && nextBtnCandidate.isEnabled()) {
//                        // Scroll into view
//                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nextBtnCandidate);
//                        Thread.sleep(300); // allow scroll to finish
//
//                        // Wait until clickable
//                        wait.until(ExpectedConditions.elementToBeClickable(nextBtnCandidate));
//
//                        // Click
//                        nextBtnCandidate.click();
//                        System.out.println("✅ Clicked visible and enabled 'Suivant' button.");
//                        nextClicked2 = true;
//                        break;
//                    }
//                } catch (Exception e) {
//                    System.out.println("⚠️ Skipping a 'Suivant' button due to error: " + e.getMessage());
//                }
//            }
//
//            if (!nextClicked2) {
//                System.out.println("❌ No visible and clickable 'Suivant' button found.");
//            }
//            takeScreenshot(driver, "11", "6th_step_initiated");
//            // 1. Find all "Enregistrer" buttons
//            List<WebElement> enregistrerButtons = driver.findElements(
//                    By.xpath("//button[contains(., 'Enregistrer') and contains(@class, 'btn-success')]")
//            );
//
//            boolean enregistrerClicked = false;
//            for (WebElement btn : enregistrerButtons) {
//                if (btn.isDisplayed() && btn.isEnabled()) {
//                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
//                    try {
//                        // Use Actions to simulate real user interaction
//                        Actions actions = new Actions(driver);
//                        actions.moveToElement(btn).pause(Duration.ofMillis(200)).click().perform();
//
//                        System.out.println("✅ Used Actions click on 'Enregistrer'");
//
//                        // Optional: Wait until spinner disappears
//                        new WebDriverWait(driver, Duration.ofSeconds(30)).until(d ->
//                                d.findElements(By.cssSelector("button.btn-success i.spinner-border")).isEmpty()
//                        );
//
//                        enregistrerClicked = true;
//                        break;
//
//                    } catch (Exception e) {
//                        System.out.println("⚠️ Click failed: " + e.getMessage());
//                    }
//                }
//            }
//
//            if (!enregistrerClicked) {
//                System.out.println("❌ No visible and enabled 'Enregistrer' button found.");
//            }
//
//            Thread.sleep(1000);
//            takeScreenshot(driver, "debug", "before_loading");
//            WebElement nodeToDoubleClick2 = wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//div[contains(@class,'node-content-wrapper') and .//span[normalize-space()='test_iheb']]")
//            ));
//            takeScreenshot(driver, "debug", "after_loading");
//
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick2);
//            new Actions(driver).doubleClick(nodeToDoubleClick2).perform();
//
//            takeScreenshot(driver, "12", "Bilan_added");

        } finally {
            if (driver != null) {
                System.out.println("done");
                driver.quit();
            }
        }
    }
}