package ru.crystal.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.crystal.driver.WebDriverManager;
import ru.crystal.pages.LetterPage;
import ru.crystal.pages.LoginPage;
import ru.crystal.pages.MailBoxPage;

public class MainTest {
    Logger log;
    WebDriver driver;
    LoginPage loginPage;
    MailBoxPage mailBoxPage;
    LetterPage letterPage;

    @DataProvider(name = "Данные для теста")
    public Object[][] emailDataProvider() throws IOException {
        Properties dataProviderXml = new Properties();
        dataProviderXml.loadFromXML(new FileInputStream("./src/test/resources/test.xml"));
        return new Object[][]{{
                String.valueOf(dataProviderXml.getProperty("subject")),
                String.valueOf(dataProviderXml.getProperty("from")),
                String.valueOf(dataProviderXml.getProperty("content"))},
        };
    }

    private WebElement GetInputLetter(String subject, String from) {
        WebElement letter = null;
        boolean flag = false;
        int pageNumber = 1;
        while (letter == null) {
            if( pageNumber > 1 ) {
                String currentUrl = driver.getCurrentUrl();
                String needCurrentUrl = "https://e.mail.ru/messages/inbox/?page="+pageNumber+"&back="+(pageNumber-1);
                if( !currentUrl.equals( needCurrentUrl ) ) {
                    driver.get(needCurrentUrl);
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    continue;
                }
            }
            WebElement bodyDIV = driver.findElement(By.className("b-datalist__body"));
            List<WebElement> inputLetters = bodyDIV.findElements(By.tagName("a"));
            for (WebElement item : inputLetters) {
                String letterSubject = item.getAttribute("data-subject");
                WebElement rootDIV = item.findElement(By.className("b-datalist__item__pic"));
                String style = rootDIV.getAttribute("style");
                String letterFrom = style.substring(style.indexOf("&email") + 7, style.indexOf("&trust"));
                if (letterSubject.equals(subject) && letterFrom.equals(from)) {
                    letter = driver.findElement(By.xpath("//a[@href='" + item.getAttribute("href") + "']"));
                    flag = true;
                    break;
                }
            }

            try {
                if (flag)
                    break;

                int oldPageNumber = pageNumber;
                WebElement paginator = driver.findElement(By.className("b-paginator__wrapper"));
                List<WebElement> pages = paginator.findElements(By.tagName("a"));
                for (WebElement item : pages) {
                    String number = item.getText();
                    if (number.equals(String.valueOf(pageNumber + 1))) {
                        item.click();
                        pageNumber += 1;
                        break;
                    }
                }
                if (oldPageNumber == pageNumber)
                    break;
            } catch (Exception e) {
                if (!flag)
                    break;
            }
        }

        return letter;
    }

    @BeforeTest
    public void Setup() throws IOException {
        log = Logger.getLogger("Test logger");
        Properties props = new Properties();
        driver = WebDriverManager.GetLocalChromeDriver(WebDriverManager.Drivers.ChromeDriver_linux32);
        loginPage = new LoginPage(driver);
        mailBoxPage = new MailBoxPage(driver);
        letterPage = new LetterPage(driver);

        props.load(new FileInputStream("./src/test/resources/login.properties"));
        String login = String.valueOf(props.getProperty("login"));
        String pass = String.valueOf(props.getProperty("pass"));
        String url = String.valueOf(props.getProperty("url"));
        log.info("Открываем страницу с почтой");
        driver.get(url);
        log.info("Логинимся");
        loginPage.Login(login, pass);
    }

    @AfterTest
    public void Exit() {
        log.info("Разлогинимся");
        mailBoxPage.Logout();
        log.info("Выйдем из браузера");
        driver.quit();
    }

    @Test(description = "Проверка письма на содержание Темы, Отправителя, Тела", dataProvider = "Данные для теста")
    public void TestMail(String subject, String from, String content) throws IOException {
        log.info("Переходим во входящие");
        mailBoxPage.ClickInbox();
        log.info("Получаем необходимое письмо");
        WebElement chosenLetter = GetInputLetter(subject, from);
        log.info("Проверка, что письмо найдено");
        Assert.assertNotEquals(chosenLetter, null, "Не найдено письмо \"" + subject + "\"");
        log.info("Откроем данное письмо");
        chosenLetter.click();
        log.info("Проверка темы письма \"" + subject + "\"");
        Assert.assertEquals(subject, letterPage.GetSubject(), "Не совпадает тема письма");
        log.info("Проверка отправителя письма \"" + from + "\"");
        Assert.assertEquals(from, letterPage.GetFrom(), "Не совпадает отправитель письма");
        log.info("Проверка тела письма");
        Assert.assertEquals(content, letterPage.GetLetterBody(), "Не совпадает тело письма");
    }
}