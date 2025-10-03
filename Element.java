package com.framework.selenium.api.design;

import org.openqa.selenium.WebElement;

public interface Element {

	void executeTheScript(String js, WebElement ele);

	void click(WebElement ele);

	void append(WebElement ele, String data);

	void clear(WebElement ele);

	void clearAndType(WebElement ele, String data);

	String getElementText(WebElement ele);

	String getBackgroundColor(WebElement ele);

	String getTypedText(WebElement ele);

	void selectDropDownUsingText(WebElement ele, String value);

	void selectDropDownUsingIndex(WebElement ele, int index);

	void selectDropDownUsingValue(WebElement ele, String value);

	boolean verifyExactText(WebElement ele, String expectedText);

	boolean verifyPartialText(WebElement ele, String expectedText);

	boolean verifyExactAttribute(WebElement ele, String attribute, String value);

	boolean verifyPartialAttribute(WebElement ele, String attribute, String value);

	boolean verifyDisplayed(WebElement ele);

	boolean verifyDisappeared(WebElement ele);
	
	boolean verifyEnabled(WebElement ele);
	
	boolean verifySelected(WebElement ele);
	

}
