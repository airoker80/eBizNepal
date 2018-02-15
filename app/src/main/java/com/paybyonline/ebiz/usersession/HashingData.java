package com.paybyonline.ebiz.usersession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingData {

	public String getSha256HashData(String dataToHash) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
			return null;
		}

		String password = dataToHash;
		md.update(password.getBytes());
		byte[] shaDig = md.digest();
		// System.out.println( new String(Hex.encode(shaDig)) );
		return byteArrayToHexString(shaDig);

	}

	public static String byteArrayToHexString(byte[] array) {
				
		String m_szUniqueID = "";
		for (int i=0;i<array.length;i++) {
			int b =  (0xFF & array[i]);
			// if it is a single digit, make sure it have 0 in front (proper padding)
			if (b <= 0xF) m_szUniqueID+="0";
			// add number to string
			m_szUniqueID+=Integer.toHexString(b); 
		}
		// hex string to uppercase
		m_szUniqueID = m_szUniqueID.toUpperCase();
				
		return m_szUniqueID;
	}

}
