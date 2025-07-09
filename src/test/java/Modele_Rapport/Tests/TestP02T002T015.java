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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class TestP02T002T015 {

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
            System.out.println("Running TestP02T002-T007");

            driver.get("https://dev.aquedi.fr/connexion");

            Properties creds = loadCredentials();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iLogin")))
                    .sendKeys(creds.getProperty("username"));
            driver.findElement(By.id("iPassword"))
                    .sendKeys(creds.getProperty("password"));
            driver.findElement(By.id("btnConnexion")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Tableau de bord']")));


            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'btnDashboard') and .//label[text()='Modèles & Rapports']]"))).click();

            WebElement nodeToDoubleClick = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'node-content-wrapper') and .//span[normalize-space()='test_iheb']]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick);
            new Actions(driver).doubleClick(nodeToDoubleClick).perform();

            Thread.sleep(2000);
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

            takeScreenshot(driver, "1", "modele_aquedi_cree");

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

            takeScreenshot(driver, "2", "drag_and_drop_checkbox");

            WebElement variableCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.card.p-2.d-flex.justify-content-between.align-items-center")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", variableCard);
            new Actions(driver)
                    .moveToElement(variableCard)
                    .pause(Duration.ofMillis(300))
                    .contextClick()
                    .perform();

            takeScreenshot(driver, "3", "right_clicked_on_first_variable_card");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(text(),'Gérer les paramètres de la variable')]")
            ));

            WebElement libelleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#varLibelle[formcontrolname='libelle']")));
            libelleInput.clear();
            libelleInput.sendKeys("iheb edited");

            WebElement agregationInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ng-select[formcontrolname='agregation'] input[type='text']")));
            agregationInput.click();
            Thread.sleep(300);
            agregationInput.sendKeys(Keys.ARROW_DOWN);
            agregationInput.sendKeys(Keys.ENTER);

            Thread.sleep(1000);
            takeScreenshot(driver, "4", "edit_variable_sidebar");

            WebElement appliquerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space()='Appliquer']]")
            ));
            appliquerBtn.click();


            WebElement ajouterOngletBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.nav-link.nav-link-add[title='Ajouter un nouvel onglet']")));
            ajouterOngletBtn.click();

            WebElement donnees2Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[contains(@class,'nav-item')]//div[contains(@class,'nav-link') and contains(., 'Données 1')]")));
            new Actions(driver).contextClick(donnees2Tab).perform();

            WebElement renommerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(., 'Renommer')]")));
            renommerBtn.click();

            WebElement renameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("li.ng-star-inserted input.form-control[type='text']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", renameInput);
            renameInput.click();
            renameInput.sendKeys("Données modifiées");

            wait.until(driver1 -> renameInput.getAttribute("value").equals("Données modifiées"));

            WebElement okBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class,'ng-star-inserted')]//button[normalize-space()='Ok']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okBtn);

            takeScreenshot(driver, "5", "renommer_donnees_input");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'nav-link')]/span[text()='Données modifiées']")));

            WebElement donnees1Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'nav-link') and contains(., 'Données 2')]")));
            new Actions(driver).contextClick(donnees1Tab).perform();

            WebElement supprimerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(., 'Supprimer')]")));
            supprimerBtn.click();

            WebElement confirmerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space()='Confirmer']")
            ));
            confirmerBtn.click();

            Thread.sleep(1000);
            takeScreenshot(driver, "6", "after_editing_1ST_tab_and_deleting_2ND");

            // 1. Click on "Propriétés générales" button
            WebElement proprieteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space()='Propriétés générales']]")
            ));
            proprieteBtn.click();

            // 2. Wait for the modal to appear (adjust selector as needed)
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'modal-content')]") // or another reliable modal locator
            ));

            // 3. Click a dropdown or input inside the modal (adjust based on what you want to press down on)
            WebElement granularityDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("select[formcontrolname='granularityCtrl']")
            ));
            Select select = new Select(granularityDropdown);
            select.selectByValue("QUOTIDIENNE"); // or any value like "HORAIRE", etc.


            // 4. Click the checkbox (using more specific selector for Material checkbox)
            WebElement checkboxVisibleLabel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("mat-checkbox[formcontrolname='sortDateDescending'] .mat-checkbox-layout")
            ));
            checkboxVisibleLabel.click();


            // 5. Click on the select (beginDateCtrl)
            WebElement selectBeginDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("select[formcontrolname='beginDateCtrl']")
            ));
            selectBeginDate.click();

            // 6. Wait for and click the "Jour" option
            WebElement jourOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//select[@formcontrolname='beginDateCtrl']/option[text()='Jour']")
            ));
            jourOption.click();

            takeScreenshot(driver, "7", "After_Openeing_Proprietes_Modal");

            // 7. Wait for and click the -0 option
            WebElement selectElement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("select[formcontrolname='beginDateValueCtrl']")
            ));

            Select dropdown = new Select(selectElement);
            dropdown.selectByValue("0");


            // 8. Wait for and click the "Jour" option for date fin
            WebElement jourOptionFin = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//select[@formcontrolname='endDateCtrl']/option[text()='Jour']")
            ));
            jourOptionFin.click();

            // 9. Wait for and click the -0 option for date fin
            WebElement selectElementend = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("select[formcontrolname='endDateValueCtrl']")
            ));

            Select dropdownend = new Select(selectElementend);
            dropdownend.selectByValue("1");


            Thread.sleep(2000);
            takeScreenshot(driver, "8", "After_filling_proprietes_Modal");
            WebElement validerButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'btn-success') and normalize-space()='Valider']")
            ));
            validerButton.click();

            Thread.sleep(1000);
            takeScreenshot(driver, "9", "After_validating_proprietes_Modal");


            WebElement affichageButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'mat-button-toggle-button') and .//mat-icon[text()='grid_view']]")
            ));
            affichageButton.click();

            Thread.sleep(1000);
            takeScreenshot(driver, "10", "After_modifying_view");


            WebElement enregistrerVarButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@id='btn.id' and .//span[normalize-space()='Enregistrer']]")
            ));
            enregistrerVarButton.click();

            Thread.sleep(1000);
            WebElement creationLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.linkText("Création modèle de rapport")
            ));
            creationLink.click();

            WebElement nodeToDoubleClick2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'node-content-wrapper') and .//span[normalize-space()='test_iheb']]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick2);
            new Actions(driver).doubleClick(nodeToDoubleClick2).perform();
            Thread.sleep(1000);

            takeScreenshot(driver, "11", "Variable_enregistré_retour_au_liste");

            WebElement versionsButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Versions' and contains(@class, 'btn-ouvrir')]")
            ));
            versionsButton.click();

            WebElement modifierButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Modifier' and contains(@class, 'btn-ouvrir')]")
            ));
            modifierButton.click();
            takeScreenshot(driver, "12", "accessing test");
            System.out.println("Matching dropdowns: " +
                    driver.findElements(By.cssSelector("select[formcontrolname='frequenceCtrl']")).size());

            // 1. Wait for dropdown to be visible
            List<WebElement> candidates = driver.findElements(By.cssSelector("select[formcontrolname='frequenceCtrl']"));
            WebElement dropdownFreq = null;

            for (WebElement el : candidates) {
                if (el.isDisplayed()) {
                    dropdownFreq = el;
                    System.out.println("Found visible dropdown!");
                    break;
                }
            }

            if (dropdownFreq == null) {
                throw new RuntimeException("No visible dropdown found among " + candidates.size() + " matches.");
            }

            // 3. Click to focus
            dropdownFreq.click();
            dropdownFreq.click();
            Thread.sleep(300); // optional but improves stability

            // 4. Use ARROW_DOWN 3 times (QUOTIDIEN > HEBDOMADAIRE > MENSUEL) and press ENTER
            new Actions(driver)
                    .sendKeys(Keys.ARROW_DOWN) // QUOTIDIEN → HEBDOMADAIRE
                    .sendKeys(Keys.ARROW_DOWN) // HEBDOMADAIRE → MENSUEL
                    .sendKeys(Keys.ENTER)      // select MENSUEL
                    .perform();

            // 5. Confirm value selected
            String selectedValue = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].value;", dropdownFreq);
            System.out.println("Selected value: " + selectedValue);

            // 6. Optional: Check the visible label
            String visibleText = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].options[arguments[0].selectedIndex].text;", dropdownFreq);
            System.out.println("Visible text: " + visibleText);

            Thread.sleep(1000);

            // 7. Screenshot
            takeScreenshot(driver, "13", "frequence_dropdown_selected");

            List<WebElement> matches = driver.findElements(By.xpath("//input[@formcontrolname='descriptionCtrl']"));
            WebElement descriptionInput = null;

            for (WebElement el : matches) {
                if (el.isDisplayed()) {
                    descriptionInput = el;
                    System.out.println(" Found visible Description input!");
                    break;
                }
            }

            if (descriptionInput == null) {
                throw new RuntimeException(" No visible Description input found among " + matches.size() + " matches.");
            }

            // Now interact
            descriptionInput.click();
            descriptionInput.clear();
            descriptionInput.sendKeys("edited");

            takeScreenshot(driver, "14", "frequence_dropdown_description");



            WebElement radioButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("input[formcontrolname='editModeCtrl'][value='model']")
                    )
            );
            radioButton.click();





            takeScreenshot(driver,"15", "After radio selection");


            WebDriver finalDriver = driver;

            // 1. Wait for the upload button ("Sélectionner un fichier") to be visible and enabled
            WebElement uploadButton = new WebDriverWait(driver, Duration.ofSeconds(30)).until(d -> {
                List<WebElement> buttons = d.findElements(By.xpath("//button[contains(@class,'btn-file') and contains(normalize-space(), 'Sélectionner un fichier')]"));
                for (WebElement btn : buttons) {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        // Optional visual highlight
                        ((JavascriptExecutor) finalDriver).executeScript("arguments[0].style.boxShadow = '0 0 0 3px orange';", btn);
                        return btn;
                    }
                }
                return null;
            });

            // 2. Get the hidden file input inside the upload button's container or nearby
            WebElement fileInput = new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
                try {
                    WebElement input = uploadButton.findElement(By.xpath(".//following::input[@type='file']"));
                    if (input != null) {
                        // Make input visible to interact
                        ((JavascriptExecutor) finalDriver).executeScript(
                                "arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible'; arguments[0].style.width='auto'; arguments[0].style.height='auto';",
                                input);
                        return input;
                    }
                } catch (Exception e) {
                    return null;
                }
                return null;
            });

            // 3. Get the file path from resources
            Path filePath = Paths.get(getClass().getClassLoader().getResource("rapport.xlsx").toURI());
            System.out.println("Uploading file from: " + filePath.toString());

            // 4. Upload file with retries
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    fileInput.sendKeys(filePath.toString());
                    String actualPath = fileInput.getAttribute("value");
                    if (actualPath != null && actualPath.contains("rapport.xlsx")) {
                        System.out.println("✅ File uploaded successfully");
                        break;
                    }
                } catch (Exception e) {
                    if (attempt == 2) throw new RuntimeException("Failed to upload file after 3 attempts", e);
                    System.out.println("⚠️ Upload attempt " + (attempt+1) + " failed, retrying...");
                    Thread.sleep(1000);
                }
            }



            List<WebElement> allBtns = driver.findElements(By.xpath("//button"));
            for (WebElement btn : allBtns) {
                String text = btn.getText().trim();
                boolean visible = btn.isDisplayed();
                boolean enabled = btn.isEnabled();
            }


            // Locate by visible text only — ignore class match (too generic)
            WebElement EnregistrerBtn = new WebDriverWait(driver, Duration.ofSeconds(20)).until(d -> {
                List<WebElement> btns = d.findElements(By.xpath("//button[normalize-space()='Enregistrer']"));
                for (WebElement b : btns) {
                    if (b.isDisplayed() && b.isEnabled()) {
                        return b;
                    }
                }
                return null;
            });


            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", EnregistrerBtn);
            Thread.sleep(300); // Let DOM settle

            try {
                EnregistrerBtn.click();
            } catch (Exception e) {
                // Fallback
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", EnregistrerBtn);
            }

            takeScreenshot(driver, "16", "after_enregistrer_click");
            Thread.sleep(2000);




            //-Choisir un mode de modification puis "Enregistrer"

            WebElement modifierButton2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Modifier' and contains(@class, 'btn-ouvrir')]")
            ));
            Thread.sleep(2000);
            modifierButton2.click();
            takeScreenshot(driver, "17", "accessing test 2");
            System.out.println("Matching dropdowns: " +
                    driver.findElements(By.cssSelector("select[formcontrolname='frequenceCtrl']")).size());

            // 1. Wait for dropdown to be visible
            List<WebElement> candidates2 = driver.findElements(By.cssSelector("select[formcontrolname='frequenceCtrl']"));
            WebElement dropdownFreq2 = null;

            for (WebElement el : candidates2) {
                if (el.isDisplayed()) {
                    dropdownFreq2 = el;
                    System.out.println("Found visible dropdown!");
                    break;
                }
            }

            if (dropdownFreq2 == null) {
                throw new RuntimeException("No visible dropdown found among " + candidates2.size() + " matches.");
            }

            // 3. Click to focus
            dropdownFreq2.click();
            dropdownFreq2.click();
            Thread.sleep(300); // optional but improves stability

            // 4. Use ARROW_DOWN 3 times (QUOTIDIEN > HEBDOMADAIRE > MENSUEL) and press ENTER
            new Actions(driver)
                    .sendKeys(Keys.ARROW_DOWN) // QUOTIDIEN → HEBDOMADAIRE
                    .sendKeys(Keys.ARROW_DOWN) // HEBDOMADAIRE → MENSUEL
                    .sendKeys(Keys.ENTER)      // select MENSUEL
                    .perform();

            // 5. Confirm value selected
            String selectedValue2 = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].value;", dropdownFreq2);
            System.out.println("Selected value: " + selectedValue);

            // 6. Optional: Check the visible label
            String visibleText2 = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].options[arguments[0].selectedIndex].text;", dropdownFreq2);
            System.out.println("Visible text: " + visibleText);

            Thread.sleep(1000);

            // 7. Screenshot
            takeScreenshot(driver, "17", "frequence_dropdown_selected");

            List<WebElement> matches2 = driver.findElements(By.xpath("//input[@formcontrolname='descriptionCtrl']"));
            WebElement descriptionInput2 = null;

            for (WebElement el : matches2) {
                if (el.isDisplayed()) {
                    descriptionInput2 = el;
                    System.out.println(" Found visible Description input!");
                    break;
                }
            }

            if (descriptionInput2 == null) {
                throw new RuntimeException(" No visible Description input found among " + matches2.size() + " matches.");
            }

            // Now interact
            descriptionInput2.click();
            descriptionInput2.clear();
            descriptionInput2.sendKeys("edited");

            takeScreenshot(driver, "18", "frequence_dropdown_description");



            WebElement radioButton2 = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("input[formcontrolname='editModeCtrl'][value='interactive']")
                    )
            );
            radioButton2.click();

            takeScreenshot(driver,"19", "After radio selection");

            List<WebElement> allBtns2 = driver.findElements(By.xpath("//button"));
            for (WebElement btn : allBtns2) {
                String text = btn.getText().trim();
                boolean visible = btn.isDisplayed();
                boolean enabled = btn.isEnabled();
            }


            // Locate by visible text only — ignore class match (too generic)
            WebElement EnregistrerBtn2 = new WebDriverWait(driver, Duration.ofSeconds(20)).until(d -> {
                List<WebElement> btns2 = d.findElements(By.xpath("//button[normalize-space()='Enregistrer']"));
                for (WebElement b : btns2) {
                    if (b.isDisplayed() && b.isEnabled()) {
                        return b;
                    }
                }
                return null;
            });


            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", EnregistrerBtn2);
            Thread.sleep(300); // Let DOM settle

            try {
                EnregistrerBtn2.click();
            } catch (Exception e) {
                // Fallback
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", EnregistrerBtn2);
            }
            Thread.sleep(1000);
            takeScreenshot(driver, "20", "after_enregistrer_click");

            wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Step 1: Wait for the link to be present in DOM
            WebElement hiddenLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.linkText("Modification modèle de rapport"))
            );

            // Step 2: Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", hiddenLink);
            Thread.sleep(500); // Allow any animation or rendering

            // Step 3: Wait until it's clickable (visible + enabled)
            WebElement visibleLink = wait.until(ExpectedConditions.elementToBeClickable(hiddenLink));

            // Step 4: Click
            visibleLink.click();
            Thread.sleep(2000);
            takeScreenshot(driver, "21", "back_to_list");

            WebElement nodeToDoubleClick3 = null;
            int maxScrollAttempts = 20;

            for (int i = 0; i < maxScrollAttempts; i++) {
                try {
                    nodeToDoubleClick3 = driver.findElement(
                            By.xpath("//div[contains(@class,'folderStyle') and .//span[@title='test_iheb']]")
                    );
                    if (nodeToDoubleClick3.isDisplayed()) {
                        break; // Found it!
                    }
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    // Not found yet — scroll and retry
                }

                // Scroll down a bit
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                Thread.sleep(500); // Wait for lazy-load/rendering
            }

            if (nodeToDoubleClick3 == null) {
                throw new RuntimeException("Could not find folder node titled 'test_iheb' after scrolling.");
            }

            // Scroll into center and double-click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nodeToDoubleClick3);
            new Actions(driver).doubleClick(nodeToDoubleClick3).perform();
            Thread.sleep(1000);

            // Re-locate the Versions button AFTER double-click
            WebElement versionsButton2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Versions' and contains(@class, 'btn-ouvrir')]")
            ));
            versionsButton2.click();
            takeScreenshot(driver, "22", "after_versions_click");


//            // 1. Click the Publier button
//            WebElement publierButton = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("button[title='Publier'].btn-valid")
//            ));
//            publierButton.click();
//
//            // 2. Wait for the modal and textarea
//            WebElement commentBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.cssSelector("textarea[formcontrolname='commentaireCtrl']")
//            ));
//            takeScreenshot(driver, "23", "after_publier_clicked");
//
//            // 3. Enter comment
//            commentBox.clear();
//            commentBox.sendKeys("new version test");
//
//            // Optional: wait a bit for form validation to trigger
//            Thread.sleep(500);
//            takeScreenshot(driver, "24", "after_commentaire_modified");
//
//            // 4. Wait until Enregistrer button is enabled and clickable
//            WebElement enregistrerButton = wait.until(driver1 -> {
//                List<WebElement> buttons = driver1.findElements(
//                        By.xpath("//button[normalize-space()='Enregistrer' and contains(@class, 'btn-success')]")
//                );
//                for (WebElement btn : buttons) {
//                    if (btn.isDisplayed() && btn.isEnabled()) {
//                        return btn;
//                    }
//                }
//                return null;
//            });
//
//            // 5. Click Enregistrer
//            enregistrerButton.click();
//            takeScreenshot(driver, "25", "after_enregistrer_clicked");
//
//            // 6. Wait for the confirmation modal and click the final "Publier" button
//            WebElement finalPublierButton = wait.until(driver1 -> {
//                List<WebElement> buttons = driver1.findElements(
//                        By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space()='Publier']")
//                );
//                for (WebElement btn : buttons) {
//                    if (btn.isDisplayed() && btn.isEnabled()) {
//                        return btn;
//                    }
//                }
//                return null;
//            });
//
//            finalPublierButton.click();
//            takeScreenshot(driver, "26", "after_publier_clicked");

            // 1. Click on "Partager" button (title="Partager")
            WebElement partagerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Partager' and contains(@class,'btn-ouvrir')]")
            ));
            partagerBtn.click();
            Thread.sleep(500);

            // 2. Wait for textarea and type comment
            WebElement commentArea = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("textarea[formcontrolname='comment']"))
            );
            commentArea.sendKeys("test partage selenium");
            Thread.sleep(500);
            // 3. Click the toggle label to enable group selection
            WebElement toggleLabel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[@for='customSwitch' and contains(text(),\"Partager avec des groupes d'utilisateurs\")]")
            ));
            toggleLabel.click();
            Thread.sleep(500);

            // 4. Wait for the combobox input to be interactable
            WebElement comboBoxInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'modal-content') and .//h5[contains(text(), 'Partager')]]//input[@role='combobox']")
            ));


            // 5. Click the input and press ENTER
            comboBoxInput.click();
            Thread.sleep(500); // Let the dropdown populate if necessary
            comboBoxInput.sendKeys(Keys.ENTER);
            takeScreenshot(driver, "23", "after_modal_partage_filled");

            // 6. Wait for "Enregistrer" button and click it
            WebElement enregistrerFinalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(), 'Enregistrer')]")
            ));
            enregistrerFinalBtn.click();
            Thread.sleep(1000);
            takeScreenshot(driver, "24", "after_partage done");


            WebElement partagerBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@title='Partager' and contains(@class,'btn-ouvrir')]")
            ));
            partagerBtn2.click();
            Thread.sleep(500);

            WebElement arreterPartageBtn = new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(@class, 'btn-delete') and normalize-space()='Arrêter le partage']")
                    )
            );
            arreterPartageBtn.click();
            Thread.sleep(500);
            takeScreenshot(driver, "25", "after_MAJ_partage_done");

            WebElement docVariablesBtn = new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@type='button' and @title='Documents ayant des variables en commun' and contains(@class, 'btn-ouvrir')]")
                    )
            );
            docVariablesBtn.click();
            Thread.sleep(500);
            takeScreenshot(driver, "26", "after_variables_communs_clicked");

            // Wait for any visible modal-content with the expected title
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'modal-content') and .//span[contains(text(), 'Liste des documents')]]")
            ));

            // Then look for the 'Fermer' button within that modal
            WebElement fermerBtn = modal.findElement(By.xpath(".//button[contains(@class, 'btn-success') and contains(text(), 'Fermer')]"));

            // Wait for the button to become clickable
            wait.until(ExpectedConditions.elementToBeClickable(fermerBtn));

            // Scroll into view to ensure no overlap
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", fermerBtn);

            // Click
            fermerBtn.click();
            takeScreenshot(driver, "27", "after_variables_communs_closed");

            // 1. Click on the "Dupliquer" button
            WebElement dupliquerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='button' and @title='Dupliquer' and contains(@class, 'btn-ouvrir')]")
            ));
            dupliquerBtn.click();

            // 2. Wait for the modal to appear and find the "Nom" input field inside it
            WebElement nomInput2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'modal-content')]//input[@formcontrolname='nom']")
            ));

            // 3. Clear and fill the input with "duplicated"
            nomInput2.clear();
            nomInput2.sendKeys("duplicated");
            Thread.sleep(300);
            takeScreenshot(driver, "28", "after_editing_name_in_duplication");

            List<WebElement> buttons = driver.findElements(By.xpath(
                    "//div[contains(@class,'modal-content')]//button[contains(@class,'btn-success')]"
            ));

            for (WebElement enregistrerBtnDup : buttons) {
                if (enregistrerBtnDup.isDisplayed() && enregistrerBtnDup.isEnabled() && enregistrerBtnDup.getText().trim().contains("Enregistrer")) {
                    enregistrerBtnDup.click();
                    break;
                }
            }
            Thread.sleep(500);
            takeScreenshot(driver, "29", "after_enregistrer_duplication");

            WebElement telechargerBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@type='button' and @title='Télécharger' and contains(@class, 'btn-ouvrir')]")
                    )
            );
            telechargerBtn.click();
            Thread.sleep(500);
            takeScreenshot(driver, "30", "after_telecharger");

            WebElement listeVariablesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='button' and @title='Liste des variables utilisées' and contains(@class, 'btn-ouvrir')]")
            ));
            listeVariablesBtn.click();
            Thread.sleep(1000); // or use explicit wait if something specific appears
            takeScreenshot(driver, "31", "after_liste_variables_display");
            driver.navigate().back();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[@title='Liste des variables utilisées']") // or any other element from previous page
            ));
            takeScreenshot(driver, "32", "after_liste_variables");











        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
