package ExtentReportScreenShot.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class AppTest 
 
{
	
	WebDriver driver;
	ExtentReports extent;
	ExtentTest extenttest;
	Logger log = Logger.getLogger(AppTest.class);
	@BeforeTest
	public void report() {
		log.info("Indide Before MEthod");
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentReport.html",true);
		extent.addSystemInfo("username", "Allu");
		extent.addSystemInfo("os","windows");
		extent.addSystemInfo("environment","Iat");
	}
	
	@AfterTest
	public void afterTest() {
		log.warn("Indide Afetr MEthod");
		extent.flush();
		extent.close();
	}
	
   @BeforeMethod
   public void setup() {
	   System.setProperty("webdriver.chrome.driver", "C:\\Users\\Lalitha\\Desktop\\PanduAutomation\\chromedriver.exe");
		driver = new ChromeDriver();
		log.info("open url");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//driver.get("https://classic.freecrm.com/index.html");
		driver.get("https://www.facebook.com/");
		log.error("taking longer time than expected");
   }
   public String screenshot(WebDriver driver,String screenshotname) throws IOException {
	   String date = new SimpleDateFormat("yyymmddhhmmss").format(new Date());
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");
		//String finalDestination = currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"
		FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + screenshotname+date+ ".png"));
		return currentDir + "/screenshots/" + screenshotname+date+ ".png";
   }
   
   @Test 
   public void verifyTitle() {
	   log.info("***********************Started***********************************");
	   extenttest=extent.startTest("verifyTitle");
	   Assert.assertEquals(driver.getTitle(), "Facebook – log in or sign up");
	   log.info("***********************Completed***********************************");
   }
   
   @AfterMethod
   public void clostest(ITestResult result ) throws IOException {
	   String screenShotPath = screenshot(driver,result.getName());
	   if(result.getStatus()==ITestResult.FAILURE) {
		   System.out.println(result.getStatus());
		   extenttest.log(LogStatus.FAIL,"Test Failed for "+result.getName());
		   extenttest.log(LogStatus.FAIL,"Test Failed for "+result.getThrowable());
		 // String screenShotPath = screenshot(driver,result.getName());
		   extenttest.log(LogStatus.FAIL,extenttest.addScreenCapture(screenShotPath));
	   }
	   else if(result.getStatus()==ITestResult.SKIP){
		   System.out.println(result.getStatus());
		   extenttest.log(LogStatus.SKIP,"Test Skipped for "+result.getName());
		  
		   extenttest.log(LogStatus.FAIL,extenttest.addScreenCapture(screenShotPath));
		   
	   }
	   else if(result.getStatus()==ITestResult.SUCCESS) {
		   System.out.println(result.getStatus());
		   extenttest.log(LogStatus.PASS,"Test Passed for "+result.getName());
		   extenttest.log(LogStatus.PASS,"Test Passed for "+result.getThrowable());
		  //String screenShotPath = screenshot(driver,result.getName());
		   extenttest.log(LogStatus.PASS,extenttest.addScreenCapture(screenShotPath));
		   
	   }
	   extent.endTest(extenttest);
	   driver.quit();
	   
   }
}
