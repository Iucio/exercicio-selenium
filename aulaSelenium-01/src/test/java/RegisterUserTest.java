import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RegisterUserTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void createDriver() {
        driver = WebDriverManager.chromedriver().create();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://automationexercise.com/");
    }

    public void abrirTelaCadastro() {

        assertTrue(driver.getTitle().contains("Automation Exercise"));

        driver.findElement(By.linkText("Signup / Login")).click();

        WebElement titulo =
                driver.findElement(
                        By.xpath("//h2[contains(text(),'New User Signup!')]")
                );

        assertTrue(titulo.isDisplayed());
    }


    public String gerarEmail() {
        return "teste" + System.currentTimeMillis() + "@email.com";
    }

    public void iniciarCadastro(String nome, String email) {

        driver.findElement(
                By.cssSelector("input[data-qa='signup-name']")
        ).sendKeys(nome);

        driver.findElement(
                By.cssSelector("input[data-qa='signup-email']")
        ).sendKeys(email);

        driver.findElement(
                By.cssSelector("button[data-qa='signup-button']")
        ).click();
    }

    public void preencherDados(String senha,
                               String dia,
                               String mes,
                               String ano,
                               String zipcode) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(
                                "//*[contains(text(),'Enter Account Information')]"
                        )
                )
        );

        driver.findElement(By.id("id_gender1")).click();

        driver.findElement(By.id("password"))
                .sendKeys(senha);

        new Select(driver.findElement(By.id("days")))
                .selectByValue(dia);

        new Select(driver.findElement(By.id("months")))
                .selectByValue(mes);

        new Select(driver.findElement(By.id("years")))
                .selectByValue(ano);

        driver.findElement(By.id("newsletter")).click();
        driver.findElement(By.id("optin")).click();

        driver.findElement(By.id("first_name"))
                .sendKeys("Lucio");

        driver.findElement(By.id("last_name"))
                .sendKeys("Junior");

        driver.findElement(By.id("company"))
                .sendKeys("UFF");

        driver.findElement(By.id("address1"))
                .sendKeys("Rua de Teste, 123");

        driver.findElement(By.id("address2"))
                .sendKeys("Apartamento 10");

        new Select(driver.findElement(By.id("country")))
                .selectByVisibleText("Canada");

        driver.findElement(By.id("state"))
                .sendKeys("Ontario");

        driver.findElement(By.id("city"))
                .sendKeys("Toronto");

        driver.findElement(By.id("zipcode"))
                .sendKeys(zipcode);

        driver.findElement(By.id("mobile_number"))
                .sendKeys("21999999999");
    }

    public void excluirContaCriada() {

        driver.findElement(
                By.cssSelector("[data-qa='continue-button']")
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Logged in as')]")
                )
        );

        driver.findElement(By.linkText("Delete Account")).click();

        WebElement contaExcluida =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("[data-qa='account-deleted']")
                        )
                );

        assertTrue(contaExcluida.isDisplayed());
    }

    @Test
    public void cadastroComEmailInvalido() {

        abrirTelaCadastro();

        WebElement nome =
                driver.findElement(
                        By.cssSelector("input[data-qa='signup-name']")
                );

        WebElement email =
                driver.findElement(
                        By.cssSelector("input[data-qa='signup-email']")
                );

        nome.sendKeys("Lucio Teste");

        email.sendKeys("testeemail.com");

        driver.findElement(
                By.cssSelector("button[data-qa='signup-button']")
        ).click();

        String mensagemValidacao =
                email.getAttribute("validationMessage");

        assertFalse(mensagemValidacao.isEmpty());
    }

    @Test
    public void cadastroComNomeVazio() {

        abrirTelaCadastro();

        WebElement nome =
                driver.findElement(
                        By.cssSelector("input[data-qa='signup-name']")
                );

        driver.findElement(
                By.cssSelector("input[data-qa='signup-email']")
        ).sendKeys(gerarEmail());

        driver.findElement(
                By.cssSelector("button[data-qa='signup-button']")
        ).click();

        String mensagemValidacao =
                nome.getAttribute("validationMessage");

        assertFalse(mensagemValidacao.isEmpty());
    }

    @Test
    public void cadastroComZipcodeVazio() {

        abrirTelaCadastro();

        iniciarCadastro(
                "Lucio Teste",
                gerarEmail()
        );

        preencherDados(
                "Senha123",
                "10",
                "5",
                "2000",
                ""
        );

        WebElement zipcode =
                driver.findElement(By.id("zipcode"));

        driver.findElement(
                By.cssSelector("button[data-qa='create-account']")
        ).click();

        String mensagemValidacao =
                zipcode.getAttribute("validationMessage");

        assertFalse(mensagemValidacao.isEmpty());
    }

    @Test
    public void cadastroComDataNascimentoInvalida() {

        abrirTelaCadastro();

        iniciarCadastro(
                "Lucio Teste",
                gerarEmail()
        );

        preencherDados(
                "Senha123",
                "31",
                "2",
                "2000",
                "12345"
        );

        driver.findElement(
                By.cssSelector("button[data-qa='create-account']")
        ).click();

        WebElement contaCriada =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("[data-qa='account-created']")
                        )
                );

        assertTrue(contaCriada.isDisplayed());

        excluirContaCriada();
    }

    @Test
    public void cadastroComSenhaUmCaractere() {

        abrirTelaCadastro();

        iniciarCadastro(
                "Lucio Teste",
                gerarEmail()
        );

        preencherDados(
                "a",
                "10",
                "5",
                "2000",
                "12345"
        );

        driver.findElement(
                By.cssSelector("button[data-qa='create-account']")
        ).click();

        WebElement contaCriada =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector("[data-qa='account-created']")
                        )
                );

        assertTrue(contaCriada.isDisplayed());

        excluirContaCriada();
    }


    @AfterEach
    public void quitDriver() {
        driver.quit();
    }
}
