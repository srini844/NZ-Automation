package com.framework.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryLogic implements IRetryAnalyzer {
	
	int maxRetry = 2;
	int iter = 1;

	@Override
	public boolean retry(ITestResult result) {
		if(iter <= maxRetry) {
			iter++;
			return true;
		}
		return false;
	}

}
