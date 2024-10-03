package ru.netology.rest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class WebUITest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }



    @Test
    void shouldSubmitApplication() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лягушеслав Болотин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        String actualText = actualElement.getText().trim();
        assertTrue(actualElement.isDisplayed());
        assertEquals(
                "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                actualText
        );
    }

    @Test
    void shouldFailIfPhoneIsTooShort() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лягушеслав Болотин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7999999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        WebElement actualElement = driver
                .findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertTrue(actualElement.isDisplayed());
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                actualText
        );
    }

    @Test
    void shouldFailIfNameContainIllegalSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лягушеслав Болотин.....");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        WebElement actualElement = driver
                .findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertTrue(actualElement.isDisplayed());
        assertEquals(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                actualText
        );
    }

    @Test
    void shouldFailIfNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        WebElement actualElement = driver
                .findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertTrue(actualElement.isDisplayed());
        assertEquals(
                "Поле обязательно для заполнения",
                actualText
        );
    }

    @Test
    void shouldFailIfPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лягушеслав Болотин");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        WebElement actualElement = driver
                .findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertTrue(actualElement.isDisplayed());
        assertEquals(
                "Поле обязательно для заполнения",
                actualText
        );
    }

    @Test
    void shouldFailIfCheckboxIsUnchecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лягушеслав Болотин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("button.button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }


}
