package com.example.tests;
import org.openqa.selenium.ie.InternetExplorerDriver;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;


@Title("Тестовое задание")
public class YaMarket {
    String company = "Acer";
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        driver = new InternetExplorerDriver();
        /*ещё пару моментов с ИЕ:
        1. Обновите браузер и его компонеты. (возможно, тесты не запускаются из-за разности браузера и скаченного драйвера.
        2. В свойствах браузера, во вкладке "Безопасность" нужно поставить галочки "Включить защищенный режим"
        3. если браузер и тесты не запускаются, проверьте маштаб ИЕ - он должен быть 100%
        4. если снова не удача - попробуйте 32битный ИЕ (если используете 64битный и наоборот)
        5. Или добавьте или найдите в реестре раздел
            HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE
            и в нём создать параметр типа DWORD с именем iexplore.exe и значением 0
            Для 64-битной версии аналогичные действия нужно проделать также с разделом
            HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE
        6. Может помочь установка драйвера в определенное место и строки:
                System.setProperty("webdriver.ie.driver", "D:\\DriversForSelenium\\IEDriverServer.exe");
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        driver = new InternetExplorerDriver(ieCapabilities);
        7. Если вышеперечисленное не помогло, замените driver = new InternetExplorerDriver(); на driver = new ChromeDriver();*/
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Step
    private void openPage()
    {
        driver.manage().window().maximize();
        driver.get("https://www.yandex.ru/");
        driver.findElement(By.linkText("Маркет")).click();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        driver.findElement(By.linkText("Компьютеры")).click();
        driver.findElement(By.xpath("(//a[contains(text(),'Планшеты')])[2]")).click();

    }
    @Step
    private void Searching()
    {
        driver.findElement(By.linkText("Перейти ко всем фильтрам")).click();
        driver.findElement(By.id("glf-pricefrom-var")).click();
        driver.findElement(By.id("glf-pricefrom-var")).clear();
        driver.findElement(By.id("glf-pricefrom-var")).sendKeys("20000");
        driver.findElement(By.id("glf-priceto-var")).click();
        driver.findElement(By.id("glf-priceto-var")).clear();
        driver.findElement(By.id("glf-priceto-var")).sendKeys("25000");
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        driver.findElement(By.xpath("/html/body/div[1]/div[4]/div/div[1]/div[1]/div[2]/div[2]/div/div[2]/button")).click();
        driver.findElement(By.xpath(".//*/div[2]/div[2]/*/span/span/input")).sendKeys(company);
        driver.findElement(By.xpath(String.format(".//div[1]/span/label[text()='%s']/..", company))).click();
        driver.findElement(By.xpath(".//*/div[2]/div[2]/*/span/span/input")).clear();
        driver.findElement(By.linkText("Показать подходящие")).click();

    }
    @Step
    private void checkAndFind()
    {
        WebElement secondNote = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div/div[1]/div[2]/div[4]/div[1]/div/a"));
        String secondNoteText = secondNote.getText();
        driver.findElement(By.id("header-search")).click();
        driver.findElement(By.id("header-search")).clear();
        driver.findElement(By.id("header-search")).sendKeys(secondNoteText);
        driver.findElement(By.id("header-search")).sendKeys(Keys.ENTER);
        WebElement resultNote = driver.findElement(By.xpath("//h1"));
        String resultText = resultNote.getText();
        System.out.print(secondNoteText + resultText);
        try {
            assertEquals(secondNoteText,resultText);
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

    }
    @Test
    @Title("Ищем планшеты")
    public void testUntitledTestCase() throws Exception {
        openPage();
        Searching();
        checkAndFind();
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}