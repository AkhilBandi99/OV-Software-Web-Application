package nl.utwente.di.OVSoftware.seleniumTest;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class OVSeleniumTest {

	/*
	 * Before running the test, make sure your path is configured properly.
	 */
	
    public static void main(String[] args) {
        String path = "/Users/ignatiuspatrick/Downloads/chromedriver";
    	System.setProperty("webdriver.chrome.driver", path);
        WebDriver driver = new ChromeDriver();

        String baseURL = "http://localhost:8080/Module4/login.html";

        driver.get(baseURL);
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginbutton = driver.findElement(By.xpath("/html/body/div/form/div[4]/button"));

        username.sendKeys("a");
        password.sendKeys("a");

        loginbutton.click();
        
        // make an if statement
        
        System.out.println(driver.getCurrentUrl());
        
        // main.html
        
        
        
    }
}
