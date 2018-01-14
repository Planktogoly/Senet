package net.marijn.senet.utils;

public class Utils {
	
	public static int isAnswerANumber(String rawAnswer) {
		try {
			return Integer.valueOf(rawAnswer);
		} catch (NumberFormatException exception) {
			return -1;
		}
	}

}
