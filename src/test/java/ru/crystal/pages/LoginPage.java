package ru.crystal.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

public class LoginPage {
    public WebDriver driver;

    public LoginPage( WebDriver driver ) {
        PageFactory.initElements( driver, this );
        this.driver = driver;
    }

    @FindBy( id = "mailbox:login" )
    private WebElement loginField;

    @FindBy( id = "mailbox:password" )
    private WebElement passwordField;

    @FindBy( id = "mailbox:submit" )
    private WebElement loginButton;

    public void InputLogin( String login ) {
        loginField.sendKeys( login );
    }

    public void InputPassword( String password ) {
        passwordField.sendKeys( password );
    }

    public void ClickLoginButton() {
        loginButton.click();
    }

    public void Login( String login, String password ) {
        InputLogin( login );
        InputPassword( password );
        ClickLoginButton();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}