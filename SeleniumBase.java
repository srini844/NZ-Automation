package com.framework.selenium.api.base;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.framework.selenium.api.design.Browser;
import com.framework.selenium.api.design.Element;
import com.framework.selenium.api.design.Locators;
import com.framework.utils.Reporter;


public class SeleniumBase extends Reporter implements Element, Browser {
	protected Actions act;
	public Properties prop;
	public static int number = 1 ;
	String exeFilePath = System.getProperty("user.dir") + "\\Documents\\FileUpload.exe";
	String pdfFilePath = System.getProperty("user.dir") + "\\Documents\\Test.pdf";
	JavascriptExecutor js= (JavascriptExecutor)getDriver();
	static String excelFilePath = "TestData/data.xlsx";
	@Override
	public void executeTheScript(String js, WebElement ele) {
		getDriver().executeScript(js, ele);
	}

	@Override
	public void click(WebElement ele) {
		try {
			
			ele.isDisplayed();
		} catch (NoSuchElementException e) {
			reportStep("Element is not found:" + ele, "fail");
		}

		String text = "";
		try {
			try {
				Thread.sleep(2000);
				getWait().until(ExpectedConditions.elementToBeClickable(ele));
				text = ele.getText();
				if (ele.isEnabled()) {
					ele.click();
				} else {
					getDriver().executeScript("arguments[0].click()", ele);
				}

			} catch (Exception e) {
				boolean bFound = false;
				int totalTime = 0;
				while (!bFound && totalTime < 10000) {
					try {
						Thread.sleep(500);
						ele.click();
						bFound = true;
					} catch (Exception e1) {
						bFound = false;
					}
					totalTime = totalTime + 500;
				}
				if (!bFound) {
					ele.click();
				}
			}
		} catch (StaleElementReferenceException e) {
			System.err.println(e);
			reportStep("Text" + text + "is unable to click due to " + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			System.err.println(e);
			reportStep("Element" + ele + "is unable to click due to " + e.getMessage(), "fail");
		} catch (Exception e) {
			System.err.println(e);
			reportStep("Element" + text + "is unable to click due to " + e.getMessage(), "fail");
		}
	}

	@Override
	public void append(WebElement ele, String data) {
		String attribute = ele.getAttribute("Value");
		try {
			if (attribute.length() > 1) {
				ele.sendKeys(data);
			} else {
				ele.sendKeys(data);
			}
		} catch (WebDriverException e) {
			reportStep("Element" + ele + "couldn't appended \n" + e.getMessage(), "fail");
		}

	}

	@Override
	public void clear(WebElement ele) {
		try {
		//	ele.clear();
			ele.sendKeys(Keys.chord(Keys.CONTROL,"A"));
			ele.sendKeys(Keys.BACK_SPACE);
		
		} catch (ElementNotInteractableException e) {
			reportStep("The field is not interactable \n" + e.getMessage(), "fail");
		}
	}

	public void clearAndType(WebElement ele, CharSequence... data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys(data);
		} catch (ElementNotInteractableException e) {
			reportStep("Element" + ele + " is not interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			pause(500);
			try {
				ele.sendKeys(data);
			} catch (Exception e1) {
				reportStep("Element" + ele + "didn't allow clear and type \n" + e.getMessage(), "fail");
			}

		}
	}

	@Override
	public String getElementText(WebElement ele) {
		
		try {
			String text = ele.getText();
			reportStep("Text has been retrieved :"+text, "info");
			return text;
		} catch (Exception e) {
			reportStep("Text is not available :"+e.getMessage(), "fail");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getBackgroundColor(WebElement ele) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTypedText(WebElement ele) {
		String attributeValue = null;
		
		try {
			attributeValue = ele.getAttribute("value");
			reportStep("Attribute value is:"+attributeValue, "info");
		} catch (Exception e) {
			reportStep("Not able to find attribute value:"+e.getMessage(), "fail");
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public void selectDropDownUsingText(WebElement ele, String value) {
		try {
			org.openqa.selenium.support.ui.Select sel = new org.openqa.selenium.support.ui.Select(ele);
			sel.selectByVisibleText(value);
			reportStep("Selected "+value+ " from the dropdown", "Pass");
		} catch (Exception e) {
			reportStep("Unable to select "+value+ " from the dropdown", "Fail");
			e.printStackTrace();
		}
		
	}

	@Override
	public void selectDropDownUsingIndex(WebElement ele, int index) {
		try {
			org.openqa.selenium.support.ui.Select sel = new org.openqa.selenium.support.ui.Select(ele);
			sel.selectByIndex(index);
			reportStep("Dropdown value is selected using Index:"+index, "pass");
		} catch (Exception e) {
			reportStep("Dropdown value is not selected by Index:"+index, "fail");
			e.printStackTrace();
		}

	}

	@Override
	public void selectDropDownUsingValue(WebElement ele, String value) {
		try {
			org.openqa.selenium.support.ui.Select sel = new org.openqa.selenium.support.ui.Select(ele);
			Thread.sleep(3000);
			sel.selectByValue(value);
			reportStep("Dropdown value is selected :"+value, "pass");
		} catch (Exception e) {
			reportStep("Not able to select drodown with value:"+value, "fail");
			e.printStackTrace();
		}

	}

	@Override
	public boolean verifyExactText(WebElement ele, String expectedText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyPartialText(WebElement ele, String expectedText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyExactAttribute(WebElement ele, String attribute, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyPartialAttribute(WebElement ele, String attribute, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyDisplayed(WebElement ele) {
		try {
			if(ele.isDisplayed()) {
				return true;
			}
		}
		catch(NoSuchElementException|ElementNotInteractableException e) {
				return false;
		}
		return false;
	
	}

	@Override
	public boolean verifyDisappeared(WebElement ele) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyEnabled(WebElement ele) {
		if(ele.isEnabled()) {
			return true;
		}
		
		return false;
	}
	public boolean verifyDisabled(WebElement ele) {
		if(ele.isEnabled()) {
			return false;
		}
		
		return true;
	}
	@Override
	public boolean verifySelected(WebElement ele) {
		// TODO Auto-generated method stub
		return false;
	}


	public void startApp(String url, String executionMode, boolean headless) {
		try {
			setDriver(prop.getProperty("browser"), executionMode);
			setWait();
			setNode();
			act = new Actions(getDriver());
			getDriver().manage().window().maximize();
			getDriver().manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			getDriver().navigate().to(url);
			reportStep(prop.getProperty("browser") + "browser launched with :" + url, "pass");
		} catch (Exception e) {
			reportStep("Unable to launch " + prop.getProperty("browser") + "browser using startApp Method", "fail");
		}
	}

	
	public void startApp(String browser, boolean headless, String url) {
		// TODO Auto-generated method stub

	}

	public WebElement locateElementByID(String Value) {
		try {
			WebElement findElement = getDriver().findElement(By.id(Value));
			return findElement;
		} catch (NoSuchElementException e) {
			reportStep("The element with ID locator " + Value + "is not found" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The element with ID locator " + Value + "is not found" + e.getMessage(), "fail");
		}
		return null;
	}

	@Override
	public List<WebElement> locateElements(Locators type, String value) {
		try {
			switch (type) {
			case CLASS_NAME:
				return getDriver().findElements(By.className(value));

			case CSS:
				return getDriver().findElements(By.cssSelector(value));

			case ID:
				return getDriver().findElements(By.id(value));

			case LINK_TEXT:
				return getDriver().findElements(By.linkText(value));

			case NAME:
				return getDriver().findElements(By.name(value));

			case PARTIAL_LINKTEXT:
				return getDriver().findElements(By.partialLinkText(value));

			case TAGNAME:
				return getDriver().findElements(By.tagName(value));

			case XPATH:
				return getDriver().findElements(By.xpath(value));
			default:
				System.err.println("Locator is not valid..Please verify");
				break;
			}
		} catch (NoSuchElementException e) {
			reportStep("Element with locator " + type + "not found with value " + e.getMessage(), "fail");
		}
		return null;
	}

	@Override
	public void switchToAlert() {

	}

	/*@Override
	public void acceptAlert() {
		String text = "";
		try {
			getWait().until(ExpectedConditions.alertIsPresent());
			Alert alert = getDriver().switchTo().alert();
			text = alert.getText();
			alert.accept();
			reportStep("Alert is dismissed", "pass", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriver Exception \n" + e.getMessage(), "fail", false);
		}
	}

	@Override
	public void dismissAlert() {
		String text = "";
		try {
			Alert alert = getDriver().switchTo().alert();
			text = alert.getText();
			alert.dismiss();
			reportStep("Alert is dismissed", "pass", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriver Exception \n" + e.getMessage(), "fail", false);
		}

	}*/



	@Override
	public void typeAlert(String data) {
		try {
			getDriver().switchTo().alert().sendKeys(data);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriver Exception \n" + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void switchToWindow(int index) {
		try {
			Set<String> allWindows = getDriver().getWindowHandles();
			List<String> allHandles = new ArrayList<String>(allWindows);
			getDriver().switchTo().window(allHandles.get(index));
			reportStep("Window with index " + index + "switched successfully", "info", false);
			reportStep(getDriver().getTitle(), "info");
		} catch (NoSuchWindowException e) {
			reportStep("Window with index " + index + "not found \n" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("Window with index " + index + "not found \n" + e.getMessage(), "fail", false);
		}
	}

	@Override
	public boolean switchToWindow(String title) {
		try {
			Set<String> windowHandles = getDriver().getWindowHandles();
			for (String eachWindow : windowHandles) {
				getDriver().switchTo().window(eachWindow);
				if (getDriver().getTitle().equals(title)) {
					break;
				}
			}
			reportStep("Window with title is switched" + title, "info");
			return true;
		} catch (NoSuchWindowException e) {
			reportStep("Window with title is not found \n" + title, "fail", false);
		}
		return false;
	}

	@Override
	public void switchToFrame(WebElement ele) {
		try {
			getDriver().switchTo().frame(ele);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("No such frame" + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void switchToFrame(String idOrName) {
		try {
			getDriver().switchTo().frame(idOrName);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("No such frame" + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void defaultContent() {
		try {
			getDriver().switchTo().defaultContent();
		} catch (Exception e) {
			reportStep("No such window" + e.getMessage(), "fail");
		}
	}

	@Override
	public boolean verifyUrl(String url) {
		if (getDriver().getCurrentUrl().equals(url)) {
			reportStep("URL " + url + " gets matched successfully", "info");
			return true;
		} else {
			reportStep("URL " + url + " not matched", "fail");
		}
		return false;
	}

	@Override
	public boolean verifyTitle(String title) {
		if (getDriver().getTitle().equals(title)) {
			reportStep("Page title" + title + " gets matched successfully", "info");
			return true;
		} else {
			reportStep("Page title" + title + " not matched", "fail");
		}
		return false;
	}

	@Override
	public void close() {
		try {
			getDriver().close();
			reportStep("Browser is closed via close method", "Info", false);
		} catch (Exception e) {
			reportStep("Browser cannot be closed via Close Method" + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void quit() {
		try {
			getDriver().quit();
			reportStep("Browser is closed via quit method", "pass", false);
		} catch (Exception e) {
			reportStep("Browser cannot be closed via Quit Method" + e.getMessage(), "fail", false);
		}
	}

//	@Override
//	public long takeSnap() {
//		System.out.println();
//		//long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
//		
//		try {
//
//			FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE),
//					new File("./reports/" + Reporter.folderName + "/images/" + number + ".png"));
//			number++;
//		} catch (WebDriverException e) {
//			reportStep("The browser has been closed." + e.getMessage(), "fail");
//			e.getMessage();
//		} catch (IOException e) {
//			reportStep("The browser has been closed." + e.getMessage(), "fail");
//			e.getMessage();
//		}
//		return number;
//	}

	public String getValueFromConfig(String key) {
		prop = new Properties();
		try {
			//prop.load(new FileInputStream("./src/main/resources/config.properties"));
			prop.load(new FileInputStream("./Config_Files/config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}
	
	public String DecodePassword(String decoder) {	
		String newDecode = new String(Base64.getDecoder().decode(decoder.getBytes()));
		return  newDecode;
		
		
	}

	public void scrollToViewElement(String val) {
		WebElement element = getDriver().findElement(By.xpath(val));
		getDriver().executeScript("arguments[0].scrollIntoView(true);", element);
	}



	public void pause(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clearAndType(WebElement ele, String data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data);
		} catch (ElementNotInteractableException e) {
			reportStep("Element" + ele + " is not interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) { // retry 1
			pause(500);
			try {
				ele.sendKeys(data);
			} catch (Exception e1) {
				reportStep("Element" + ele + "didn't allow clear and type \n" + e.getMessage(), "fail");
			}

		}

	}

	public void typeAndTab(WebElement ele, String data) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			//ele.sendKeys("", "", Keys.TAB);
			ele.sendKeys(data, Keys.TAB);
		} catch (ElementNotInteractableException e) {
			reportStep("Element" + ele + " is not interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) { // retry 1
			pause(500);
			try {
				ele.sendKeys(data);
			} catch (Exception e1) {
				reportStep("Element" + ele + "is not interactable \n" + e.getMessage(), "fail");
			}

		}

	}
	public void clearandTab(WebElement ele, Keys tab) {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			//ele.sendKeys("", "", Keys.TAB);
			ele.sendKeys("",Keys.TAB);
		} catch (ElementNotInteractableException e) {
			reportStep("Element" + ele + " is not interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) { // retry 1
			pause(500);
			try {
				ele.sendKeys(tab);
			} catch (Exception e1) {
				reportStep("Element" + ele + "is not interactable \n" + e.getMessage(), "fail");
			}

		}

	}

	public void type(WebElement ele, String data) throws InterruptedException {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data);
		} catch (ElementNotInteractableException e) {
			reportStep("Element is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("Element is not Interactable \n" + e.getMessage(), "fail");
		}

	}
	public void typeandClear(WebElement ele, String data) throws InterruptedException {
		try {
			getWait().until(ExpectedConditions.visibilityOf(ele));
			ele.sendKeys(data);
			ele.click();
			ele.sendKeys(Keys.chord(Keys.CONTROL,"A"));
			ele.sendKeys(Keys.BACK_SPACE);
			//ele.clear();
			//ele.sendKeys("", data,Keys.TAB);			
			//ele.clear();
		} catch (ElementNotInteractableException e) {
			reportStep("Element is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("Element is not Interactable \n" + e.getMessage(), "fail");
		}

	}
	@Override
	public WebElement locateElement(Locators locatorType, String value) {
		try {
			switch (locatorType) {
			case CLASS_NAME:
				return getDriver().findElement(By.className(value));

			case CSS:
				return getDriver().findElement(By.cssSelector(value));

			case ID:
				return getDriver().findElement(By.id(value));

			case LINK_TEXT:
				return getDriver().findElement(By.linkText(value));

			case NAME:
				return getDriver().findElement(By.name(value));

			case PARTIAL_LINKTEXT:
				return getDriver().findElement(By.partialLinkText(value));

			case TAGNAME:
				return getDriver().findElement(By.tagName(value));

			case XPATH:
				return getDriver().findElement(By.xpath(value));
			default:
				System.err.println("Locator is not valid..Please verify");
				break;
			}
		} catch (NoSuchElementException e) {
			reportStep("Element with locator " + locatorType + "not found with value " + e.getMessage(), "fail");
		}
		return null;
	}
	
	public String randomStringGeneration() {
			String alphabet = "109283746512093487567964328769";
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			int length = 9;
			for(int i = 0; i < length; i++) {
				int index = random.nextInt(alphabet.length());
				sb.append(alphabet.charAt(index));	
			}
			return ("00000"+sb.toString()).substring(0, 9);
			

	}
	public String OriginCountry_randomStringGeneration() {
		String alphabet = "zxwertyuabcdefghijklmnopqrstuvwxzyabcdesfewdjhgggfcvjss";
		char[] charArray = alphabet.toCharArray();
		
		
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int length = 2;
		for(int i = 0; i < length; i++) {
			//int index = random.nextInt(charArray[].length());
			int index = random.nextInt(alphabet.length());
			
			sb.append(alphabet.charAt(index));	
		}
		return (""+sb.toString()).substring(0, 2);
		

}
	public String randomStringGenerationCid() {
		
		String alphabet = "109283746512093487567964328769";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int length = 8;
		for(int i = 0; i < length; i++) {
			int index = random.nextInt(alphabet.length());
			sb.append(alphabet.charAt(index));	
		}						
		return ("00000"+sb.toString()).substring(0, 8);

}
public String AccMaintenance_randomStringGeneration() {
		
		String alphabet = "109283746512093487567964328769";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int length = 6;
		for(int i = 0; i < length; i++) {
			int index = random.nextInt(alphabet.length());
			sb.append(alphabet.charAt(index));	
		}						
		return ("0"+sb.toString()).substring(0, 6);

}

public String Chargecode_randomStringGeneration() {
	String alphabet = "zxwertyuabcdefghijklmnopqrstuvwxzyabcdesfewdjhgggfcvjss";
	char[] charArray = alphabet.toCharArray();
	
	
	StringBuilder sb = new StringBuilder();
	Random random = new Random();
	int length = 4;
	for(int i = 0; i < length; i++) {
		//int index = random.nextInt(charArray[].length());
		int index = random.nextInt(alphabet.length());
		
		sb.append(alphabet.charAt(index));	
	}
	return (""+sb.toString()).substring(0, 4);
	

}

		@Override
		public long takeSnap() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public void enterValueIntoTextField(WebElement element, String data) {
			String readyState = documentReadyState();
			try {
				if(readyState.equalsIgnoreCase("complete")) {
					try {
						waitTillElementVisible(element);
						String labelName = element.getAttribute("name");
						element.clear();
						element.sendKeys(data);
						
								
				} catch (NoSuchElementException e) {
					refreshWebPage();
					waitTillElementVisible(element);
					String labelName = element.getAttribute("name");
					element.clear();
					element.sendKeys(data);
					throw e;
				}
				catch (Exception e) {			
					throw e;
				}
				}
			} catch (TimeoutException ex) {
				refreshWebPage();
				
				waitTillElementVisible(element);
				String labelName = element.getAttribute("name");
				element.clear();
				element.sendKeys(data);
							}
			
			
			
		}
		
		public void enterValueIntoTextField1(WebElement element, String data) {
			try {
					try {
						waitTillElementVisible(element);
						String labelName = element.getAttribute("name");
						element.clear();
						element.sendKeys(data);
						
								
				} catch (NoSuchElementException e) {
					refreshWebPage();
					
					waitTillElementVisible(element);
					String labelName = element.getAttribute("name");
					element.clear();
					element.sendKeys(data);
					
					throw e;
				}
				catch (Exception e) {			
					
					throw e;
				}
				
			} catch (TimeoutException ex) {
				refreshWebPage();
				
				waitTillElementVisible(element);
				String labelName = element.getAttribute("name");
				element.clear();
				element.sendKeys(data);
				
			}
			
			
			
		}


		/**
		 * Method for clicking on WebElements such as Button, RadioButton, CheckBox,
		 * Links, etc
		 * 
		 * @param element
		 */
		public void clickElement(WebElement element) {
			try {
				waitTillElementVisible(element);
				String labelName = element.getAttribute("title");
				element.click();
				
			} catch (Exception e) {
				
			}
		}

		// ======================= WEBDRIVER WAITS ======================= //

		/**
		 * Method to wait for particular time till the Element is Visible
		 * 
		 * @param element
		 * @return
		 */
		public WebElement waitTillElementVisible(WebElement element) {
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
				wait.until(ExpectedConditions.visibilityOf(element));
			} catch (TimeoutException e) {
				 e.printStackTrace();
				
			} catch (Exception e) {
				 e.printStackTrace();
				
			}
			return element;
		}

		/**
		 * Method to wait for particular time till the Element is Visible
		 * 
		 * @param element
		 * @param seconds
		 */
		public void waitTillElementVisible(WebElement element, int seconds) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
			wait.until(ExpectedConditions.visibilityOf(element));
		}

		/**
		 * Method to wait for particular time till the Element is Not Visible
		 * 
		 * @param element
		 */
		public void waitTillElementInVisible(WebElement element, int seconds) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
			wait.until(ExpectedConditions.invisibilityOf(element));
		}

		/**
		 * Method to wait for particular time till the Element is Clickable
		 * 
		 * @param element
		 */
		public void elementToBeClickable(WebElement element) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}

		// ======================= JAVA SCRIPT EXECUTOR ======================= //

		/**
		 * Method for clicking on WebElement using Java Script
		 * 
		 * @param element
		 */
		public void clickElementUsingJavaScript(WebElement element) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				js.executeScript("arguments[0].click();", element);
			} catch (Exception e) {
				throw e;
			}
		}

		/**
		 * Method for scrolling the web page until the specific element is in View
		 */
		public  void scrollIntoViewUsingJavaScript(WebElement element) {
			try {
//				waitTillElementVisible(element);
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				js.executeScript("arguments[0].scrollIntoView()", element);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		/**
		 * Method for scrolling up the web page by specified pixel
		 */
		public  void scrollUpUsingJavaScript() {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("window.scrollBy(0,-2000)");
		}

		/**
		 * Method for scrolling down the web page by specified pixel
		 */
		public void scrollDownUsingJavaScript() {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("window.scrollBy(0,10000)");
		}

		/**
		 * Method for scrolling the document in the window by specified value
		 */
		public void scrollByUsingJavaScript() {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		}

		// ======================= ACTION CLASS ======================= //

		/**
		 * Method for performing mouse Hover action
		 * 
		 * @param element
		 */
		public void mouseHover(WebElement element) {
			try {
				Actions builder = new Actions((getDriver()));
				builder.moveToElement(element).build().perform();
				
			} catch (Exception e) {
			
			}
		}

		/**
		 * Method for performing mouse Hover action and click on the WebElement
		 * 
		 * @param element
		 */
		public void mouseHoverAndClick(WebElement element) {
			try {
				Actions builder = new Actions(getDriver());
				builder.moveToElement(element).click().perform();
				//log.info("Mouse Hover on the WebElement and performed click action Successfully");
			} catch (Exception e) {
				//log.error("Fialed to Mouse Hover and click on the WebElement", e);
			}
		}

		/**
		 * Method for performing Double Click action on the WebElement
		 * 
		 * @param element
		 */
		public void doubleClick(WebElement element) {
			try {
				Actions builder = new Actions(getDriver());
				builder.doubleClick(element).perform();
				//log.info("Successfully performed Double Click on the WebElement");
			} catch (Exception e) {
				//log.error("Failed to perform Double Click Action on the WebElement", e);
				// e.printStackTrace();
			}
		}

		/**
		 * Method for Drag and Drop actions on the WebElement
		 * 
		 * @param sourceElement
		 * @param targetElement
		 */
		public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
			try {
				Actions builder = new Actions((getDriver()));
				builder.dragAndDrop(sourceElement, targetElement).build().perform();
				//log.info("Successfully performed Drag and Drop Action on the WebElement");
			} catch (Exception e) {
				//log.error("Failed to perform Drag and Drop Action on the WebElement", e);
			}
		}

		// ======================= SELECT CLASS ======================= //

		/**
		 * Method for selecting the value using Displayed Text
		 * 
		 * @param element
		 * @param text
		 */
		public void selectUsingVisibleText(WebElement element, String text) {
			try {
				waitTillElementVisible(element);
				String labelName = element.getAttribute("name");
				Select dropDown = new Select(element);
				dropDown.selectByVisibleText(text);
				//log.info("Successfully selected value '" + text + "' in the '" + labelName + "' dropdown");
			} catch (Exception e) {
				//log.error("Failed to select value '" + text + "' from the drop down'", e);
				// e.printStackTrace();
			}
		}

		/**
		 * Method for printing all the options available in the drop down
		 * 
		 * @param element
		 */
		public static void getOptions(WebElement element) {
			Select objSelect = new Select(element);
			List<WebElement> DropDownValues = objSelect.getOptions();
			for (WebElement options : DropDownValues) {
				// System.out.println(options.getText());
				//log.info(options.getText());
			}
		}

		/**
		 * Method for retrieving the currently selected Option from the drop down
		 * 
		 * @param element
		 * @return
		 */
		public String getFirstSelectedOption(WebElement element) {
			Select objSelect = new Select(element);
			WebElement option = objSelect.getFirstSelectedOption();
			String selectedoption = option.getText();
			// System.out.println("Selected element: " + selectedoption);
			//log.info("Selected element: " + selectedoption);
			return selectedoption;
		}

		// ======================= ALERT CLASS ======================= //

		/**
		 * Method for switching to alert and accepting it
		 */
		public void acceptAlert() {
			getDriver().switchTo().alert().accept();
			//log.info("Handled the alert successfully");
		}

		/**
		 * Method for switching to alert and rejecting it
		 */
		public void dismissAlert() {
			getDriver().switchTo().alert().dismiss();
			//log.info("Handled the alert successfully");
		}

		/**
		 * Method for switching to alert and getting text
		 */
		public void getAlertText() {
			String text = getDriver().switchTo().alert().getText();
			System.out.println("Alert text is : " +text);
		}

		// ======================= VERIFICATION ======================= //

		/**
		 * Method for verifying the element is Visible
		 * 
		 * @param element
		 */
		public void verifyElementPresent(WebElement element) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
			try {
				wait.until(ExpectedConditions.visibilityOf(element));
				//log.info(element + "was found");
			} catch (Exception e) {
				//log.error("Unable to find the element" + element + ":" + e);
			}
		}

		/**
		 * Method for verifying the element is Visible and returning true or false
		 * 
		 * @param element
		 * @return
		 */
		public boolean checkElementIsVisible(WebElement element) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		}

		/**
		 * Method for validating the element is Editable
		 * 
		 * @param element
		 */
		public static void Enabled(WebElement element) {
			Assert.assertEquals(true, element.isEnabled(), "Element is not editable");
			Assert.assertEquals(true, element.isDisplayed(), "Element is not Displayed");
		}

		/**
		 * Method for validating the element is Non-Editable
		 * 
		 * @param element
		 */
		public static void NonEditable(WebElement element) {
			String readonly = element.getAttribute("readonly");
			Assert.assertNull(readonly, "Text field is Editable");
		}

		/**
		 * Method for verifying if 2 Strings are equal
		 * 
		 * @param firstString
		 * @param secondString
		 */
		public static void verifyStringsAreEqual(String firstString, String secondString) {
			//log.info(firstString + "<equals>" + secondString);
			Assert.assertEquals(firstString, secondString);
		}

		// ======================= ROBOT CLASS ======================= //

		/**
		 * Method for performing Zoom Out Action using Robot Class
		 * 
		 * @throws AWTException
		 */
		public static void zoomOutUsingRobotClass() throws AWTException {
			Robot robot = new Robot();
			for (int i = 1; i <= 3; i++) {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_MINUS);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_MINUS);
			}
		}

		/**
		 * Method to 'Upload File' using 'Robot Class'
		 * 
		 * @param filePath
		 * @throws AWTException
		 */
		public void uploadFileUsingRobotClass(String filePath) throws AWTException {
			StringSelection stringSelection = new StringSelection(filePath);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
			Robot robot;
			try {
				robot = new Robot();
				robot.delay(3000);
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.delay(3000);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_V);
				robot.delay(3000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.delay(3000);
				System.out.println("!!! Document Uploaded Successfully !!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// ======================= AUTO IT ======================= //

		/**
		 * Method to 'Upload File' using 'Auto IT'
		 * 
		 * @throws IOException
		 */
		public void uploadFileUsingAutoIT() throws IOException {
			try {
				Runtime.getRuntime().exec(exeFilePath + " " + pdfFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// @After
		public void tearDown() {
			getDriver().quit();
		}

		public String captureScreenshot(String scenarioName) throws IOException {
			String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) getDriver();
			File source = ts.getScreenshotAs(OutputType.FILE);
			String destination = System.getProperty("user.dir") + "\\screenshots\\" + scenarioName + "_" + timeStamp
					+ ".png";
			try {
				FileUtils.copyFile(source, new File(destination));
			} catch (Exception e) {
				e.getMessage();
			}
			return destination;
		}

		public byte[] getByteScreenshot() throws IOException {
			File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
			byte[] fileContent = FileUtils.readFileToByteArray(src);
			return fileContent;
		}
		
		/**
		 * Method to check the value in dropdown
		 * 
		 * @param eleSelectParam
		 * @param Value
		 * @throws InterruptedException
		 */
		public static boolean value_In_Dropdown(WebElement eleSelectParam, String Value) throws InterruptedException {
			Select eleSelect = new Select(eleSelectParam);
			List<WebElement> eleOptions = eleSelect.getOptions();
			for(WebElement eleOption:eleOptions)
			{
				if(eleOption.getText().trim().equals(Value)) {
					return true;
				}
			}
			return false;
			
		}
		
		public void refreshWebPage() {
			getDriver().navigate().refresh();		
		}
		
		public  String documentReadyState() {
			return (String) js.executeScript("return document.readyState");
			
		}	
		
	
	
		
		public void doubleClick_Shipment(WebElement element) throws InterruptedException {
			try {
				Thread.sleep(4000);
				waitTillElementVisible(element);
				Thread.sleep(4000);
				String javascript = "var clickEvent  = document.createEvent ('MouseEvents');\r\n"
						+ "clickEvent.initEvent ('dblclick', true, true);\r\n" + "arguments[0].dispatchEvent (clickEvent);";
				// double click on the element
				JavascriptExecutor javascriptExecutor = (JavascriptExecutor) getDriver();
				javascriptExecutor.executeScript(javascript, element);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public int getRowCount(String sheetName) throws IOException {
			XSSFWorkbook workbook;
			XSSFSheet sheet;
			FileInputStream inputStream = null;
					inputStream = new FileInputStream(new File(excelFilePath));
			workbook = new XSSFWorkbook(inputStream);
			sheet = workbook.getSheet(sheetName);
			sheet = workbook.getSheet(sheetName);
			int rowcount = sheet.getPhysicalNumberOfRows();
			//System.out.println("Total number of rows:"+rowcount);
			inputStream.close();
			return rowcount;
		}
		
		
		
		

}
