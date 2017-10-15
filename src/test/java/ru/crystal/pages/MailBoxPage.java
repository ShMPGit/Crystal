package ru.crystal.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MailBoxPage {
    public WebDriver driver;

    public MailBoxPage( WebDriver driver ) {
        PageFactory.initElements( driver, this );
        this.driver = driver;
    }

    @FindBy( id = "PH_logoutLink" )
    private WebElement logoutButton;

    @FindBy( xpath = "//a[@href='/messages/inbox/']")
    private WebElement inbox;

    public void Logout() {
        logoutButton.click();
    }

    public void ClickInbox() {
        inbox.click();
    }
}