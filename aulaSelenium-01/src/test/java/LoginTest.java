import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest {

    protected WebDriver driver;

    private final String EMAIL_EXISTENTE = "luciomartelojr@hotmail.com";

    @BeforeEach
    public void createDriver() {
        driver = WebDriverManager.chromedriver().create();
        driver.get("https://automationexercise.com/");
    }

    public void abrirTelaLogin() {

        assertTrue(driver.getTitle().contains("Automation Exercise"));

        driver.findElement(By.linkText("Signup / Login")).click();

        WebElement tituloLogin =
                driver.findElement(
                        By.xpath("//h2[contains(text(),'Login to your account')]")
                );

        assertTrue(tituloLogin.isDisplayed());
    }

    @Test
    public void loginComEmailExistenteESenhaIncorreta() {

        abrirTelaLogin();

        driver.findElement(
                By.cssSelector("input[data-qa='login-email']")
        ).sendKeys(EMAIL_EXISTENTE);

        driver.findElement(
                By.cssSelector("input[data-qa='login-password']")
        ).sendKeys("senhaIncorreta123");

        driver.findElement(
                By.cssSelector("button[data-qa='login-button']")
        ).click();

        WebElement mensagemErro =
                driver.findElement(
                        By.xpath(
                                "//p[contains(text(),'Your email or password is incorrect!')]"
                        )
                );

        assertTrue(mensagemErro.isDisplayed());
    }

    @Test
    public void loginComEmailFormatoInvalido() {

        abrirTelaLogin();

        WebElement email =
                driver.findElement(
                        By.cssSelector("input[data-qa='login-email']")
                );

        email.sendKeys("testeemail.com");

        driver.findElement(
                By.cssSelector("input[data-qa='login-password']")
        ).sendKeys("senha123");

        driver.findElement(
                By.cssSelector("button[data-qa='login-button']")
        ).click();
        String mensagemValidacao =
                email.getAttribute("validationMessage");

        assertFalse(mensagemValidacao.isEmpty());
    }

    @Test
    public void loginComEmailVazio() {

        abrirTelaLogin();

        WebElement email =
                driver.findElement(
                        By.cssSelector("input[data-qa='login-email']")
                );

        driver.findElement(
                By.cssSelector("input[data-qa='login-password']")
        ).sendKeys("senha123");

        driver.findElement(
                By.cssSelector("button[data-qa='login-button']")
        ).click();
        String mensagemValidacao =
                email.getAttribute("validationMessage");

        assertFalse(mensagemValidacao.isEmpty());
    }

    @Test
    public void loginComSenhaVazia() {

        abrirTelaLogin();

        driver.findElement(
                By.cssSelector("input[data-qa='login-email']")
        ).sendKeys(EMAIL_EXISTENTE);

        WebElement senha =
                driver.findElement(
                        By.cssSelector("input[data-qa='login-password']")
                );

        driver.findElement(
                By.cssSelector("button[data-qa='login-button']")
        ).click();
        String mensagemValidacao =
                senha.getAttribute("validationMessage");

        assertFalse(mensagemValidacao.isEmpty());
    }


    @Test
    public void loginComSenhaUmCaractere() {

        abrirTelaLogin();

        driver.findElement(
                By.cssSelector("input[data-qa='login-email']")
        ).sendKeys(EMAIL_EXISTENTE);

        driver.findElement(
                By.cssSelector("input[data-qa='login-password']")
        ).sendKeys("a");

        driver.findElement(
                By.cssSelector("button[data-qa='login-button']")
        ).click();

        WebElement mensagemErro =
                driver.findElement(
                        By.xpath(
                                "//p[contains(text(),'Your email or password is incorrect!')]"
                        )
                );

        assertTrue(mensagemErro.isDisplayed());
    }


    @AfterEach
    public void quitDriver() {
        driver.quit();
    }
}
