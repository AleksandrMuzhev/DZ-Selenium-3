package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DebitCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Александр Мужев-Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79120009999");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id=order-success]")).getText();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldValidateNameForm() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Aleksandr9148$%!@#%");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79120009999");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldNoNameForm() {
        driver.findElement(new By.ByCssSelector("[data-test-id=phone] input")).sendKeys("79120009999");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldValidatePhoneForm() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Александр Мужев-Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7912000");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    public void shouldNoPhoneForm() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Александр Мужев-Петров");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(new By.ByCssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldInvalidCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Александр Мужев-Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79120009999");
        driver.findElement(By.className("button")).click();
        WebElement checkbox = driver.findElement(new By.ByCssSelector("[data-test-id=agreement].input_invalid"));
        checkbox.isDisplayed();
    }
}