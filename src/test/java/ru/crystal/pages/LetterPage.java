package ru.crystal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LetterPage {
    public WebDriver driver;

    public LetterPage( WebDriver driver ) {
        PageFactory.initElements( driver, this );
        this.driver = driver;
    }

    @FindBy( className = "b-letter__head__subj__text" )
    private WebElement subject;

    @FindBy( className = "b-letter__head__addrs__from" )
    private WebElement from;

    @FindBy( className = "b-letter__body" )
    private WebElement letterBody;

    public String GetSubject() {
        return subject.getText();
    }

    public String GetFrom() {
        return from.findElement(By.tagName("span")).getAttribute("data-contact-informer-email");
    }

    public String GetLetterBody() {
        return letterBody.findElements(By.tagName("div")).get(5).getText();
    }
}