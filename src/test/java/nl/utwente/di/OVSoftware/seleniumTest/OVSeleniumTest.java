package nl.utwente.di.OVSoftware.seleniumTest;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class OVSeleniumTest {

	/*
	 * Before running the test, make sure your path is configured properly.
	 */

	public static void main(String[] args) throws InterruptedException {
		String path = "/Users/ignatiuspatrick/Downloads/chromedriver";
		System.setProperty("webdriver.chrome.driver", path);
		WebDriver driver = new ChromeDriver();

		String baseURL = "http://localhost:8080/Module4/";

		driver.get(baseURL);
		WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));
		WebElement loginbutton = driver.findElement(By.xpath("/html/body/div/form/div[4]/button"));

		username.sendKeys("a");
		password.sendKeys("a");

		loginbutton.click();
		// wait 1s for selenium to load
		Thread.sleep(1000);
		// make an if statement
		if (driver.getCurrentUrl().contains("main.html")) {
			System.out.println("Login is succesful!");
		} else {
			System.out.println("Login failed.");
		}

		// main.html elements
		WebElement sidebarbut = driver.findElement(By.id("menu-toggle"));
		WebElement empnum = driver.findElement(By.id("searchid"));
		WebElement empname = driver.findElement(By.id("searchname"));
		WebElement statusbut = driver.findElement(By.id("statusButton"));
		// TODO: add status dropdown menu
		WebElement statusA = driver.findElement(By.id("statusA"));
		WebElement statusI = driver.findElement(By.id("statusI"));
		WebElement statusH = driver.findElement(By.id("statusH"));
		WebElement searchbut = driver.findElement(By.id("searchmain"));
		WebElement officebut = driver.findElement(By.id("officeButton"));
		WebElement importbut = driver.findElement(By.id("import"));
		WebElement export = driver.findElement(By.id("export"));
		WebElement logoutbutton = driver.findElement(By.id("logout"));
		WebElement result = driver.findElement(By.id("EmployeeList"));

		// test search function
		empnum.sendKeys("5");
		empname.sendKeys("MaSt");
		searchbut.click();

		Thread.sleep(1000);

		if (result.getText().contains("5") && result.getText().contains("MaSt")) {
			System.out.println("Search succeeded.");
		} else {
			System.out.println("Malfunction on search button.");
		}
		
		empnum.clear();
		empname.clear();
		statusbut.click();
		statusA.click();
		
		Thread.sleep(1000);
		
		
		if (result.getText().contains("Not Active") || result.getText().contains("Not Active Yet")) {
			System.out.println("searching active employee failed");
		} else {
			System.out.println("searching active employee success");
		}
		
		// TODO: test import, export, and office function
		
		logoutbutton.click();
		Thread.sleep(1000);
		
		if(driver.getCurrentUrl().contains("login.html")) {
			System.out.println("log out is succesful");
		} else {
			System.out.println("log out is unsuccessful");
		}
		
	}
}
