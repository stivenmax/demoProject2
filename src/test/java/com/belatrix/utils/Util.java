package com.belatrix.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	public static Float parseTextToNumber(String valueToConvert) {
		String result = "0";
		Pattern pattern = Pattern.compile("[0-9]*[,. ][0-9]*[,.][0-9]*");
		Matcher matcher = pattern.matcher(valueToConvert);
		if(matcher.find()) {
			result = matcher.group(0).replaceAll(" ", "").replaceAll(",", "");
		}
		

		return Float.valueOf(result);
	}
	
	}
