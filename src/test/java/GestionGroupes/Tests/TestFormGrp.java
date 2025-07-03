package GestionGroupes.Tests;

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

public class TestFormGrp {

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
    public void runTestFormGrp() throws Exception {
        WebDriver driver = null;

        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            driver.get("https://dev.aquedi.fr/connexion");
            System.out.println("Running Test formulaire grp");



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
                    By.xpath("//div[contains(@class,'btnDashboard') and .//label[text()='Référentiel']]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'card') and .//h5[text()='Gestion des groupes']]"))).click();

            takeScreenshot(driver, "after_accessing_page");

            WebElement groupeBilanTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("formulaires-tab")));
            groupeBilanTab.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Ajouter un groupe']"))).click();

            WebElement libelleInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[formcontrolname='libelleCtrl']")));
            libelleInput.clear();
            libelleInput.sendKeys("test-iheb1");

            WebElement groupDropdownInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("app-select-better input[type='text']")));
            groupDropdownInput.click();
            groupDropdownInput.sendKeys(Keys.ENTER);

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.ng-dropdown-panel")));

            WebElement ajouterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'modal-footer')]//button[contains(text(),'Ajouter')]")));
            System.out.println("DEBUG: Clicking 'Ajouter' button");
            ajouterButton.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-content")));
            System.out.println("DEBUG: Modal closed");

            takeScreenshot(driver, "after_adding_group");

            WebElement viewport = driver.findElement(By.cssSelector(".slick-viewport"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", viewport);

            new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
                for (WebElement cell : d.findElements(By.cssSelector("div.slick-cell.l0"))) {
                    try {
                        String text = cell.getText().trim();
                        System.out.println("Found cell: " + text);
                        if (text.equalsIgnoreCase("test-iheb1")) {
                            return true;
                        }
                    } catch (StaleElementReferenceException ignored) {}
                }
                return false;
            });

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", viewport);

            WebElement editButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
                for (WebElement cell : d.findElements(By.cssSelector("div.slick-cell.l0"))) {
                    try {
                        if (cell.getText().trim().equalsIgnoreCase("test-iheb1")) {
                            WebElement row = cell.findElement(By.xpath("./ancestor::div[contains(@class,'slick-row')]"));
                            return row.findElement(By.cssSelector("div.slick-cell.l2 button.btn-ouvrir"));
                        }
                    } catch (StaleElementReferenceException ignored) {}
                }
                return null;
            });

            System.out.println("DEBUG: Clicking edit button for 'test-iheb1'");
            editButton.click();

            WebElement libelleInputEdit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[formcontrolname='libelleCtrl']")));
            libelleInputEdit.clear();
            libelleInputEdit.sendKeys("test-iheb1-edited");

            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'modal-footer')]//button[contains(text(),'Enregistrer')]")));
            saveButton.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-content")));
            System.out.println("DEBUG: Group edited successfully.");

            takeScreenshot(driver, "after_editing_group");

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight",
                    driver.findElement(By.cssSelector(".slick-viewport")));

            // 🔁 SAFE DELETE BUTTON FINDER BLOCK (Avoids StaleElementReferenceException)
            WebElement deleteButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
                try {
                    for (WebElement cell : d.findElements(By.cssSelector("div.slick-cell.l0"))) {
                        try {
                            if (cell.getText().trim().equalsIgnoreCase("test-iheb1-edited")) {
                                WebElement row = cell.findElement(By.xpath("./ancestor::div[contains(@class,'slick-row')]"));
                                try {
                                    return row.findElement(By.cssSelector("button.btn-remove"));
                                } catch (NoSuchElementException e) {
                                    WebElement trashIcon = row.findElement(By.cssSelector("button i.icon-remove"));
                                    return trashIcon.findElement(By.xpath("./ancestor::button"));
                                }
                            }
                        } catch (StaleElementReferenceException ignored) {}
                    }
                } catch (StaleElementReferenceException ignored) {}
                return null;
            });

            System.out.println("DEBUG: Clicking delete button for 'test-iheb1-edited'");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", deleteButton);
            deleteButton.click();

            Thread.sleep(500); // let modal animate

            takeScreenshot(driver, "before_deletion_confirmation");

            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Confirmer')]")
            ));
            confirmButton.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-content")));

            wait.until(driver1 -> driver1.findElements(
                    By.xpath("//div[contains(@class, 'slick-cell') and text()='test-iheb1-edited']")).isEmpty());

            takeScreenshot(driver, "after_deleting_group");

            System.out.println("Group 'test-iheb1-edited' successfully deleted.");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
