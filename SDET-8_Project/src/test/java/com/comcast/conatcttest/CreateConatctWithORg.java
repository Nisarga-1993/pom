package com.comcast.conatcttest;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comcast.commonutils.ExcelUtility;
import com.comcast.commonutils.FileUtility;
import com.comcast.commonutils.JavaUtils;
import com.comcast.commonutils.WebDriverUTils;

public class CreateConatctWithORg {
	
	@Test
	public void createConatctWithORg() throws Throwable {
		WebDriverUTils wLib = new WebDriverUTils();
		FileUtility fLib = new FileUtility();
		ExcelUtility elib = new ExcelUtility();
		
		/*Common  Data*/
		String URL  = fLib.getPropertyKeyValue("url");
		String USERNAME  = fLib.getPropertyKeyValue("username");
		String PASSWORD  = fLib.getPropertyKeyValue("password");
		String BROWSER  = fLib.getPropertyKeyValue("browser");
		
		/*Test  Data*/
		String orgNAme = elib.getExcelData("Contact", "tc_01", "OrgName")+JavaUtils.getRanDomData() ;
		String orgIndustry = elib.getExcelData("Contact", "tc_01", "Industry");
		String orgType = elib.getExcelData("Contact", "tc_01", "Type");
		String orgRating = elib.getExcelData("Contact", "tc_01", "Rating");
		String contactLastNAme  = elib.getExcelData("Contact", "tc_01", "LastName")+JavaUtils.getRanDomData();
		

		/*step 1 : login to app*/
		WebDriver driver ;
		 if(BROWSER.equals("chrome")) {
		    driver = new ChromeDriver();
		 }else if(BROWSER.equals("firefox")) {
			 driver = new FirefoxDriver();
		 }else if(BROWSER.equals("ie")) {
			 driver = new InternetExplorerDriver();
		 }else {
			 driver = new ChromeDriver(); 
		 }
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get(URL);
		
		driver.findElement(By.name("user_name")).sendKeys(USERNAME);
		driver.findElement(By.name("user_password")).sendKeys(PASSWORD);
		driver.findElement(By.id("submitButton")).click();
		
		
		/*step 2 : Navigtae to Organization*/
		driver.findElement(By.linkText("Organizations")).click();
		
		/*step 3 : navigate to create Org Page*/
		driver.findElement(By.xpath("//img[@title='Create Organization...']")).click();
		
		/*step 4 : Create new Organization*/
		driver.findElement(By.name("accountname")).sendKeys(orgNAme);
		wLib.select(driver.findElement(By.name("industry")), orgIndustry);
		wLib.select(driver.findElement(By.name("accounttype")), orgType);
		wLib.select(driver.findElement(By.name("rating")), orgRating);
		driver.findElement(By.xpath("//input[@title='Save [Alt+S]']")).click();
		
	   /*verify*/
		String actOrgNAmeSuccessFullMsg = driver.findElement(By.xpath("//span[@class='dvHeaderText']")).getText();
         Assert.assertTrue(actOrgNAmeSuccessFullMsg.contains(orgNAme));
         
         /*step 5 : Navigtae to Contacts*/
         driver.findElement(By.linkText("Contacts")).click();
         
 		/*step 6 : navigate to create Contact Page*/
 		driver.findElement(By.xpath("//img[@title='Create Contact...']")).click();
 		
		/*step 7 : Create new Contact With Organization*/
			driver.findElement(By.name("lastname")).sendKeys(contactLastNAme);
			driver.findElement(By.xpath("//input[@name='account_name']/following-sibling::img")).click();
			
			//swicth to child Wondow 
			wLib.switchToWindow(driver, "Accounts&action");
			driver.findElement(By.name("search_text")).sendKeys(orgNAme);
			driver.findElement(By.name("search")).click();
			driver.findElement(By.xpath("//a[text()='"+orgNAme+"']")).click();
			//swicth to Parent Wondow
			wLib.switchToWindow(driver, "Contacts");  
			driver.findElement(By.xpath("//input[@title='Save [Alt+S]']")).click();
		
		 /*step 8 : logout*/ 
			WebElement wb = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
			wLib.moveMouseToElemnet(driver, wb);
			driver.findElement(By.linkText("Sign Out")).click();
			driver.quit();
	}

}







