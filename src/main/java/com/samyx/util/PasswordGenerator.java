package com.samyx.util;

import java.security.SecureRandom;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class PasswordGenerator {
	static char[] LOWERCASE = (new String("abcdefghijklmnopqrstuvwxyz")).toCharArray();

	static char[] UPPERCASE = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();

	static char[] NUMBERS = (new String("0123456789")).toCharArray();

	static char[] ALL_CHARS = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
			.toCharArray();

	static Random rand = new SecureRandom();

	public String GeneraPwApi(int length) {
		assert length >= 4;
		char[] password = new char[length];
		password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
		password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
		password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
		int i;
		for (i = 4; i < length; i++)
			password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
		for (i = 0; i < password.length; i++) {
			int randomPosition = rand.nextInt(password.length);
			char temp = password[i];
			password[i] = password[randomPosition];
			password[randomPosition] = temp;
		}
		return (new String(password)).replaceAll(" ", "");
	}
}
