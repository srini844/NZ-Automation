package com.framework.selenium.api.design;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface Browser {
	

	public void startApp(String browser, boolean headless,String url);
	
	public WebElement locateElement(Locators locatorType, String Value);
		
	public List<WebElement> locateElements(Locators locatorType, String value);
	
	public void switchToAlert();
	
	public void acceptAlert();
	
	public void dismissAlert();
	
	public void getAlertText();
	
	public void typeAlert(String data);
	
	public void switchToWindow(int index);
	
	public boolean switchToWindow(String title);
	
	public void switchToFrame(WebElement ele);
	
	public void switchToFrame(String idOrName);
	
	public void defaultContent();
	
	public boolean verifyUrl(String url);
	
	public boolean verifyTitle(String title);
	
	public void close();
	
	public void quit();
	
	
	
	

}
