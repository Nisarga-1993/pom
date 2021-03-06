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
import com.comcast.objectrepositorylib.Contacts;
import com.comcast.objectrepositorylib.CreateNewConatct;
import com.comcast.objectrepositorylib.CreateNewOrganization;
import com.comcast.objectrepositorylib.Home;
import com.comcast.objectrepositorylib.Login;
import com.comcast.objectrepositorylib.OrganizationInfo;
import com.comcast.objectrepositorylib.Organizations;

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
		
		/*step 1 : login to app*/
		Login lp = new Login(driver);
		lp.loginToApp(USERNAME, PASSWORD);
		
		/*step 2 : navigate to  Organization page */
		Home hp = new Home(driver);
		hp.getOrgLnk().click();
		
		/*step 3 : navigate to create Organization Page*/
		Organizations orgPage = new Organizations(driver);
		orgPage.getCreateOrgImg().click();
		
		/*step 4 : create a Organization*/
		CreateNewOrganization cno = new CreateNewOrganization(driver);
		cno.creatOrganization(orgNAme, orgIndustry, orgType, orgRating);
		
		/*verify */
		OrganizationInfo orginfo = new OrganizationInfo(driver);
		String actMsg = orginfo.getSuccessFullMsg().getText();
		Assert.assertTrue(actMsg.contains(orgNAme));
		
		/*step 5 : navigate to Contact Page*/
		wLib.waitForElemnetToBeClickable(driver , hp.getContactLnk());
		hp.getContactLnk().click();
		
		/*step 6 : navigate to create Contact Page*/
		Contacts cp = new Contacts(driver);
		cp.getCreateOrgImg().click();
		
		/*step 7 create Conatct with Org Name*/
		CreateNewConatct cnc = new CreateNewConatct(driver);
		cnc.createContact(contactLastNAme, orgNAme);
		
		
	}

}







