package me.creepinson.utils;

public class RomanNumberUtils {

	   public static int unnumeral(String number) {
	        if (number.startsWith("M")) return 1000 + unnumeral(number.replaceFirst("M", ""));
	        if (number.startsWith("CM")) return 900 + unnumeral(number.replaceFirst("CM", ""));
	        if (number.startsWith("D")) return 500 + unnumeral(number.replaceFirst("D", ""));
	        if (number.startsWith("CD")) return 400 + unnumeral(number.replaceFirst("CD", ""));
	        if (number.startsWith("C")) return 100 + unnumeral(number.replaceFirst("C", ""));
	        if (number.startsWith("XC")) return 90 + unnumeral(number.replaceFirst("XC", ""));
	        if (number.startsWith("L")) return 50 + unnumeral(number.replaceFirst("L", ""));
	        if (number.startsWith("XL")) return 40 + unnumeral(number.replaceFirst("XL", ""));
	        if (number.startsWith("X")) return 10 + unnumeral(number.replaceFirst("X", ""));
	        if (number.startsWith("IX")) return 9 + unnumeral(number.replaceFirst("IX", ""));
	        if (number.startsWith("V")) return 5 + unnumeral(number.replaceFirst("V", ""));
	        if (number.startsWith("IV")) return 4 + unnumeral(number.replaceFirst("IV", ""));
	        if (number.startsWith("I")) return 1 + unnumeral(number.replaceFirst("I", ""));
	        return 0;
	    }

	    public static String numeral(int number) {
	        if (number<=0) return "";
	        if (number-1000>=0) return "M" + numeral(number-1000);
	        if (number-900>=0) return "CM" + numeral(number-900);
	        if (number-500>=0) return "D" + numeral(number-500);
	        if (number-400>=0) return "CD" + numeral(number-400);
	        if (number-100>=0) return "C" + numeral(number-100);
	        if (number-90>=0) return "XC" + numeral(number-90);
	        if (number-50>=0) return "L" + numeral(number-50);
	        if (number-40>=0) return "XL" + numeral(number-40);
	        if (number-10>=0) return "X" + numeral(number-10);
	        if (number-9>=0) return "IX" + numeral(number-9);
	        if (number-5>=0) return "V" + numeral(number-5);
	        if (number-4>=0) return "IV" + numeral(number-4);
	        if (number-1>=0) return "I" + numeral(number-1);
	        return null;
	    }
	    // Or

	    public String numural(int number) {
	        String[] symbols = new String[] { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
	        int[] numbers = new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
	        for (int i = 0; i < numbers.length; i++) {
	            if (number >= numbers[i]) {
	                return symbols[i] + numural(number - numbers[i]);
	            }
	        }
	        return "";
	    }
	 
	    public int unnumural(String number) {
	        String[] symbols = new String[] { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
	        int[] numbers = new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
	        for (int i = 0; i < symbols.length; i++) {
	            if (number.startsWith(symbols[i])) {
	                return numbers[i] + unnumural(number.replaceFirst(symbols[i], ""));
	            }
	        }
	        return 0;
	    }
	
}
