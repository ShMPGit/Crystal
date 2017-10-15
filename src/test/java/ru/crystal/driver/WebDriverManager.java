package ru.crystal.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverManager {
    public enum Drivers {
        ChromeDriver_linux32,
        ChromeDriver_linux64
    }

    public static WebDriver GetLocalChromeDriver( Drivers driver ) {
        switch ( driver ) {
            case ChromeDriver_linux32:
                System.setProperty( "webdriver.chrome.driver", "./src/test/resources/chromedriver_linux_32" );
                break;
            case ChromeDriver_linux64:
                System.setProperty( "webdriver.chrome.driver", "./src/test/resources/chromedriver_linux_64" );
                break;
        }

        return new ChromeDriver();
    }
}