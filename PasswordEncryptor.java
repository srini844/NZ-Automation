package com.framework.utils;

import java.util.Base64;

import org.testng.annotations.Test;

public class PasswordEncryptor {

	@Test
	public void encryptor() {

		String encrptData = "LangadaPall!12";

		byte[] encodedBytes = Base64.getEncoder().encode(encrptData.getBytes());

		System.out.println("encodedBytes --------------->" + new String(encodedBytes));
	}

}
