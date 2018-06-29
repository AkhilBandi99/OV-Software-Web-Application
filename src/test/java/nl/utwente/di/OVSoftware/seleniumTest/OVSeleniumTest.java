package nl.utwente.di.OVSoftware.seleniumTest;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class OVSeleniumTest {

	/*
	 * Before running the test, make sure your path is configured properly.
	 */

	private static final String PATH = "/Users/ignatiuspatrick/Downloads/chromedriver";

	private static WebDriver driver;
	private static final String LOGIN_URL = "http://localhost:8080/Module4/login.html";
	private static final String MAIN_URL = "http://localhost:8080/Module4/main.html";
	private static final String ADMIN_URL = "http://localhost:8080/Module4/admin.html";
	static WebElement username;
	static WebElement password;
	static WebElement loginbutton;
	static WebElement sidebarbut;
	static WebElement empnum;
	static WebElement empname;
	static WebElement statusbut;
	static WebElement statusA;
	static WebElement statusI;
	static WebElement statusH;
	static WebElement searchbut;
	static WebElement officebut;
	static WebElement importbut;
	static WebElement export;
	static WebElement logoutbutton;
	static WebElement resultEmp;
	static List<WebElement> resultEmpName;
	static List<WebElement> resultEmpStatuses;
	static List<WebElement> resultEmpIds;

	public static void setUpDriver() {
		System.setProperty("webdriver.chrome.driver", PATH);
		driver = new ChromeDriver();
	}

	public static void loadPage(String url) {
		try {
			driver.get(url);
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void constructLoginElements() {
		username = driver.findElement(By.id("username"));
		password = driver.findElement(By.id("password"));
		loginbutton = driver.findElement(By.xpath("/html/body/div/form/div[4]/button"));
	}

	public static void constructMainElements() {
		sidebarbut = driver.findElement(By.id("menu-toggle"));
		empnum = driver.findElement(By.id("searchid"));
		empname = driver.findElement(By.id("searchname"));
		statusbut = driver.findElement(By.id("statusButton"));
		statusA = driver.findElement(By.id("statusA"));
		statusI = driver.findElement(By.id("statusI"));
		statusH = driver.findElement(By.id("statusH"));
		searchbut = driver.findElement(By.id("searchmain"));
		officebut = driver.findElement(By.id("officeButton"));
		importbut = driver.findElement(By.id("import"));
		export = driver.findElement(By.id("export"));
		logoutbutton = driver.findElement(By.id("logout"));
		resultEmp = driver.findElement(By.id("EmployeeList"));
	}

	public static void testLoginOVAccount() {
		username.sendKeys("a");
		password.sendKeys("a");

		loginbutton.click();
		try {
			Thread.sleep(500);
			if (driver.getCurrentUrl().contains("main.html")) {
				System.out.println("Login is succesful!");
			} else {
				System.out.println("Login failed.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void testSearchFunction() {
		empnum.sendKeys("5");
		empname.sendKeys("MaSt");
		searchbut.click();

		try {
			Thread.sleep(500);
			if (resultEmp.getText().contains("MaSt") && resultEmp.getText().contains("5")) {
				System.out.println("search succeeded.");
			} else {
				System.out.println("search failed.");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void testStatusFunction() {

		try {
			loadPage(MAIN_URL);
			constructMainElements();
			// test status active
			statusbut.click();
			statusA.click();
			Thread.sleep(500);
			resultEmpStatuses = driver.findElements(By.id("employeestatus"));
			for (WebElement e : resultEmpStatuses) {
				if (!e.getText().equals("Active")) {
					System.out.println("searching active employee failed");
					break;
				}
			}

			// test status inactive
			statusbut.click();
			statusI.click();

			Thread.sleep(500);
			resultEmpStatuses = driver.findElements(By.id("employeestatus"));
			for (WebElement e : resultEmpStatuses) {
				if (!e.getText().equals("Not Active")) {
					System.out.println("searching inactive employee failed");
					break;
				}
			}

			// test status not active yet
			statusbut.click();
			statusH.click();

			Thread.sleep(500);
			resultEmpStatuses = driver.findElements(By.id("employeestatus"));
			for (WebElement e : resultEmpStatuses) {
				if (!e.getText().equals("Not Active Yet")) {
					System.out.println("searching not active yet employee failed");
					break;
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Test status function done.");

	}

	public static void testLogOut() {

		try {
			constructMainElements();
			logoutbutton.click();
			Thread.sleep(500);
			if (driver.getCurrentUrl().contains("login.html")) {
				System.out.println("log out is succesful");
			} else {
				System.out.println("log out is unsuccessful");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void testDeletePayrate() {
		try {
			Thread.sleep(500);
			
			WebElement deletepaybut = driver.findElement(By.xpath("//*[@id=\"infotable\"]/tr[2]/td[5]/span"));
			deletepaybut.click();
			Thread.sleep(500);
			
			Alert alt = driver.switchTo().alert();
			alt.accept();
			
			WebElement closepopbut = driver.findElement(By.xpath("//*[@id=\"empInfo\"]/div/div/div[1]/button/span"));
			closepopbut.click();
			
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void testEditPayrate() {
		try {
			Thread.sleep(500);
			WebElement editpaybut = driver.findElement(By.xpath("//*[@id=\"infotable\"]/tr[2]/td[4]/span"));
			editpaybut.click();
			WebElement epayrate = driver.findElement(By.xpath("//*[@value=\"45\"]"));
			WebElement epayratefrom = driver.findElement(By.xpath("//*[@value=\"2016-12-01\"]"));
			WebElement epayrateuntil = driver.findElement(By.xpath("//*[@value=\"2017-01-01\"]"));
			
			epayrate.clear();
			epayratefrom.clear();
			epayrateuntil.clear();
			
			epayrate.sendKeys("55");
			epayratefrom.sendKeys("2016-12-01");
			epayrateuntil.sendKeys("2018-01-01");
			
			WebElement savebut = driver.findElement(By.xpath("//*[@id=\"empInfo\"]/div/div/div[3]/button[3]"));
			savebut.click();
			Thread.sleep(500);
			
			WebElement closepopbut = driver.findElement(By.xpath("//*[@id=\"empInfo\"]/div/div/div[1]/button/span"));
			closepopbut.click();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void testAddPayrate() {
		try {
			Thread.sleep(500);
			WebElement addpayratebut = driver.findElement(By.xpath("//*[@id=\"empInfo\"]/div/div/div[3]/button[1]"));
			addpayratebut.click();
			WebElement newpayrate = driver.findElement(By.xpath("//*[@value=\"Cost\"]"));
			WebElement newpayratefrom = driver.findElement(By.xpath("//*[@value=\"Start date\"]"));
			WebElement newpayrateuntil = driver.findElement(By.xpath("//*[@value=\"End date\"]"));
			
			newpayrate.clear();
			newpayratefrom.clear();
			newpayrateuntil.clear();
			
			newpayrate.sendKeys("45");
			newpayratefrom.sendKeys("2016-12-01");
			newpayrateuntil.sendKeys("2017-01-01");
			
			WebElement savebut = driver.findElement(By.xpath("//*[@id=\"empInfo\"]/div/div/div[3]/button[3]"));
			savebut.click();
			Thread.sleep(500);
			
			WebElement closepopbut = driver.findElement(By.xpath("//*[@id=\"empInfo\"]/div/div/div[1]/button/span"));
			closepopbut.click();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public static void testOfficeFunction() {
		try {
			officebut.click();
			Thread.sleep(500);
			WebElement belgieoff = driver.findElement(By.xpath("//*[@id=\"officeDropdown\"]/a[2]"));
			belgieoff.click();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void testImportFunction() {
		
	}
	
	public static void testExportFunction() {
		
	}
	
	public static void main(String[] args) {
		setUpDriver();
		loadPage(LOGIN_URL);
		// test login.html functions
		constructLoginElements();
		testLoginOVAccount();
		// testLoginGoogleAccount();

		// test main.html functions
		constructMainElements();
		//testSearchFunction();
		//testStatusFunction();

		//loadPage(MAIN_URL);

		WebElement popupr1 = driver.findElement(By.xpath("//*[@id=\"EmployeeList\"]/tr[1]"));
		popupr1.click();
		testAddPayrate();
		
		popupr1.click();
		testEditPayrate();
		
		popupr1.click();
		testDeletePayrate();

		testOfficeFunction();
		testImportFunction();
		testExportFunction();
		
		loadPage(MAIN_URL);
		testLogOut();

	}

}
