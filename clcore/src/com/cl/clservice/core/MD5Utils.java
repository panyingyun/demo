package com.cl.clservice.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Utils
 * @Description: MD5Utils
 */
public class MD5Utils {
	public static String toMd5(byte[] bytes) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest(), "");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			hexString.append(Integer.toHexString(0xFF & b)).append(separator);
		}
		return hexString.toString();
	}
}
