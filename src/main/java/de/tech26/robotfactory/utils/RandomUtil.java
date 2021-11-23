package de.tech26.robotfactory.utils;

import java.security.SecureRandom;

/**
 * RandomUtil is intended for simple use cases like generate random code
 * 
 * @author Jagadheeswar Reddy
 *
 */
public class RandomUtil {

	/**
	 * Creates a random alpha numeric string whose length is the number of
	 * characters specified.
	 * 
	 * @param len
	 * @return
	 */
	public static String generateCode(int len) {
		final String source = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

		SecureRandom random = new SecureRandom();
		StringBuilder code = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			code.append(source.charAt(random.nextInt(source.length())));
		}
		return code.toString();
	}
}
