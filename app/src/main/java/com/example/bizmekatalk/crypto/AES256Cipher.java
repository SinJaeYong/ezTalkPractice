package com.example.bizmekatalk.crypto;

import android.util.Base64;
import android.util.Log;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Cipher {


	public static String stringToEncryptString(String msg) {

		String MethodName = "stringToEncryptString";

		initKey();

		return stringToEncryptStringWithBizmeka(msg);

	}

	public static String stringToDecryptionString(String msg) {
		String MethodName = "stringToDecryptionString";

		return stringToDecryptionStringWithBizmeka(msg);

	}



	private static byte[] SECRET_KEY_JAVA_BYTE;
	private static byte[] IV_JAVA_BYTE;
	private static byte[] SECRET_KEY_BIZMEKA_BYTE;
	private static byte[] IV_BIZMEKA_BYTE;

	public static void initKey() {
		try {
			if (SECRET_KEY_BIZMEKA_BYTE == null)
				SECRET_KEY_BIZMEKA_BYTE = hex2byte(getAPB_BIZMEKA());
			if (IV_BIZMEKA_BYTE == null)
				IV_BIZMEKA_BYTE = hex2byte(getIV_BIZMEKA());

			if (SECRET_KEY_JAVA_BYTE == null)
				SECRET_KEY_JAVA_BYTE = getAPB_JAVA().getBytes("UTF-8");
			if (IV_JAVA_BYTE == null)
				IV_JAVA_BYTE = getIV_JAVA().getBytes("UTF-8");


		} catch (Exception e) {
			Log.i("jay.AES256Cipher","initKey  : fail");
		}
	}

	public static String stringToEncryptStringWithJava(String s) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY_JAVA_BYTE, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV_JAVA_BYTE));

			byte[] encryptedData = cipher.doFinal(s.getBytes("UTF-8"));

			return Base64.encodeToString(encryptedData, 0).trim();
		} catch (Exception e) {
			return s;
		}
	}

	public static String stringToEncryptStringWithBizmeka(String s) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY_BIZMEKA_BYTE, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV_BIZMEKA_BYTE));

			byte[] encryptedData = cipher.doFinal(s.getBytes("UTF-8"));

			return Base64.encodeToString(encryptedData, 0).trim();
		} catch (Exception e) {
			return s;
		}
	}

	public static String stringToDecryptionStringWithJava(String s) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY_JAVA_BYTE, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(IV_JAVA_BYTE));

			byte[] decryptedData = cipher.doFinal(Base64.decode(s, 0));

			return new String(decryptedData, "UTF-8");
		} catch (Exception e) {
			return s;
		}
	}

	public static String stringToDecryptionStringWithBizmeka(String s) {
		//CustomLog.d("stringToDecryptionStringWithBizmeka before : " + s);
		try {
			Log.i("jay.AES256Cipher","SECRET_KEY_BIZMEKA_BYTE  : " + SECRET_KEY_BIZMEKA_BYTE);
			SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY_BIZMEKA_BYTE, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(IV_BIZMEKA_BYTE));

			byte[] decryptedData = cipher.doFinal(Base64.decode(s, Base64.DEFAULT));
			String resultStr = new String(decryptedData, "UTF-8");
			// 공백문자열 처리
			if ("+KWRZhLK2WM1j6ak0RIGQw==".equals(s) && resultStr.length() == 0) {
				return "";
			} else if (resultStr.length() == 0) {
				return s;
			} else {
				return resultStr;
			}
		} catch (Exception e) {
			Log.i("jay.AES256Cipher","DECRYPT EXCEPTION  : " + e);
			return s;
		}
	}

    //16진수 to byte
    private static byte[] hex2byte(String hex) {
        if (hex == null || hex.length() == 0) { return null; }
        byte[] ba = new byte[hex.length() / 2];

        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    // APB BIZMEKA
    // "f4150d4a1ac5708c29e437749045a39a"
	private static final String APB_BIZMEKA_01_A = "f4";
	private static final String APB_BIZMEKA_01_B = "15";
	private static final String APB_BIZMEKA_01_C = "0d";
	private static final String APB_BIZMEKA_01_D = "4a";
	private static final String APB_BIZMEKA_02_A = "1a";
	private static final String APB_BIZMEKA_02_B = "c5";
	private static final String APB_BIZMEKA_02_C = "70";
	private static final String APB_BIZMEKA_02_D = "8c";
	private static final String APB_BIZMEKA_03_A = "29";
	private static final String APB_BIZMEKA_03_B = "e4";
	private static final String APB_BIZMEKA_03_C = "37";
	private static final String APB_BIZMEKA_03_D = "74";
	private static final String APB_BIZMEKA_04_A = "90";
	private static final String APB_BIZMEKA_04_B = "45";
	private static final String APB_BIZMEKA_04_C = "a3";
	private static final String APB_BIZMEKA_04_D = "9a";
	private static String getAPB_BIZMEKA() {
		String[] arrStr = new String[] {
                APB_BIZMEKA_01_A, APB_BIZMEKA_04_D, APB_BIZMEKA_01_C, APB_BIZMEKA_04_B,
                APB_BIZMEKA_02_A, APB_BIZMEKA_03_D, APB_BIZMEKA_02_C, APB_BIZMEKA_03_B,
                APB_BIZMEKA_03_A, APB_BIZMEKA_02_D, APB_BIZMEKA_03_C, APB_BIZMEKA_02_B,
                APB_BIZMEKA_04_A, APB_BIZMEKA_01_D, APB_BIZMEKA_04_C, APB_BIZMEKA_01_B};
		return getAllStr(arrStr);
	}

	// IV BIZMEKA
	// "86afc43868fea6abd40fbf6d5ed50905"
	private static final String IV_BIZMEKA_01_A = "86";
	private static final String IV_BIZMEKA_01_B = "af";
	private static final String IV_BIZMEKA_01_C = "c4";
	private static final String IV_BIZMEKA_01_D = "38";
	private static final String IV_BIZMEKA_02_A = "68";
	private static final String IV_BIZMEKA_02_B = "fe";
	private static final String IV_BIZMEKA_02_C = "a6";
	private static final String IV_BIZMEKA_02_D = "ab";
	private static final String IV_BIZMEKA_03_A = "d4";
	private static final String IV_BIZMEKA_03_B = "0f";
	private static final String IV_BIZMEKA_03_C = "bf";
	private static final String IV_BIZMEKA_03_D = "6d";
	private static final String IV_BIZMEKA_04_A = "5e";
	private static final String IV_BIZMEKA_04_B = "d5";
	private static final String IV_BIZMEKA_04_C = "09";
	private static final String IV_BIZMEKA_04_D = "05";
	private static String getIV_BIZMEKA() {
		String[] arrStr = new String[] {
				IV_BIZMEKA_01_A, IV_BIZMEKA_04_D, IV_BIZMEKA_01_C, IV_BIZMEKA_04_B,
				IV_BIZMEKA_02_A, IV_BIZMEKA_03_D, IV_BIZMEKA_02_C, IV_BIZMEKA_03_B,
				IV_BIZMEKA_03_A, IV_BIZMEKA_02_D, IV_BIZMEKA_03_C, IV_BIZMEKA_02_B,
				IV_BIZMEKA_04_A, IV_BIZMEKA_01_D, IV_BIZMEKA_04_C, IV_BIZMEKA_01_B};
		return getAllStr(arrStr);
	}

	// APB JAVA
	// "BEE6A8052A1BAD5BB1E40F4C0F8B1FD2"
	private static final String APB_JAVA_01_A = "BE";
	private static final String APB_JAVA_01_B = "E6";
	private static final String APB_JAVA_01_C = "A8";
	private static final String APB_JAVA_01_D = "05";
	private static final String APB_JAVA_02_A = "2A";
	private static final String APB_JAVA_02_B = "1B";
	private static final String APB_JAVA_02_C = "AD";
	private static final String APB_JAVA_02_D = "5B";
	private static final String APB_JAVA_03_A = "B1";
	private static final String APB_JAVA_03_B = "E4";
	private static final String APB_JAVA_03_C = "0F";
	private static final String APB_JAVA_03_D = "4C";
	private static final String APB_JAVA_04_A = "0F";
	private static final String APB_JAVA_04_B = "8B";
	private static final String APB_JAVA_04_C = "1F";
	private static final String APB_JAVA_04_D = "D2";
	private static String getAPB_JAVA() {
		String[] arrStr = new String[] {
				APB_JAVA_01_A, APB_JAVA_04_D, APB_JAVA_01_C, APB_JAVA_04_B,
				APB_JAVA_02_A, APB_JAVA_03_D, APB_JAVA_02_C, APB_JAVA_03_B,
				APB_JAVA_03_A, APB_JAVA_02_D, APB_JAVA_03_C, APB_JAVA_02_B,
				APB_JAVA_04_A, APB_JAVA_01_D, APB_JAVA_04_C, APB_JAVA_01_B};
		return getAllStr(arrStr);
	}

	// IV JAVA
	// "5F1F6B63AAA65002"
	private static final String IV_JAVA_01_A = "5F";
	private static final String IV_JAVA_01_B = "1F";
	private static final String IV_JAVA_01_C = "6B";
	private static final String IV_JAVA_01_D = "63";
	private static final String IV_JAVA_02_A = "AA";
	private static final String IV_JAVA_02_B = "A6";
	private static final String IV_JAVA_02_C = "50";
	private static final String IV_JAVA_02_D = "02";
	private static String getIV_JAVA() {
		String[] arrStr = new String[] {
				IV_JAVA_01_A, IV_JAVA_02_D, IV_JAVA_01_C, IV_JAVA_02_B,
				IV_JAVA_02_A, IV_JAVA_01_D, IV_JAVA_02_C, IV_JAVA_01_B};
		return getAllStr(arrStr);
	}

	private static String getAllStr(String[] strArr) {
		StringBuilder sb = new StringBuilder();
		int k = 0;
		for (int i = 0; i < strArr.length; i++) {
			if (k % 2 == 0) {
				sb.append(strArr[k]);
			} else {
				sb.append(strArr[strArr.length - k]);
			}
			k++;
		}
		return sb.toString();
	}
}
