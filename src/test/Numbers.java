package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Numbers {
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z' };

	final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>();

	static {
		for (int i = 0; i < digits.length; i++) {
			digitMap.put(digits[i], (int) i);
		}
	}

	/**
	 * ֧�ֵ���������
	 */
	public static final int MAX_RADIX = digits.length;

	/**
	 * ֧�ֵ���С������
	 */
	public static final int MIN_RADIX = 2;

	/**
	 * ����������ֵת��Ϊָ���Ľ����������֧��62���ƣ���ĸ�����Ѿ��þ���
	 * 
	 * @param i
	 * @param radix
	 * @return
	 */
	public static String toString(long i, int radix) {
		if (radix < MIN_RADIX || radix > MAX_RADIX)
			radix = 10;
		if (radix == 10)
			return Long.toString(i);

		final int size = 65;
		int charPos = 64;

		char[] buf = new char[size];
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = digits[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (size - charPos));
	}

	static NumberFormatException forInputString(String s) {
		return new NumberFormatException("For input string: \"" + s + "\"");
	}

	/**
	 * ���ַ���ת��Ϊ����������
	 * 
	 * @param s
	 *            �����ַ���
	 * @param radix
	 *            ������
	 * @return
	 */
	public static long toNumber(String s, int radix) {
		if (s == null) {
			throw new NumberFormatException("null");
		}

		if (radix < MIN_RADIX) {
			throw new NumberFormatException("radix " + radix
					+ " less than Numbers.MIN_RADIX");
		}
		if (radix > MAX_RADIX) {
			throw new NumberFormatException("radix " + radix
					+ " greater than Numbers.MAX_RADIX");
		}

		long result = 0;
		boolean negative = false;
		int i = 0, len = s.length();
		long limit = -Long.MAX_VALUE;
		long multmin;
		Integer digit;

		if (len > 0) {
			char firstChar = s.charAt(0);
			if (firstChar < '0') {
				if (firstChar == '-') {
					negative = true;
					limit = Long.MIN_VALUE;
				} else if (firstChar != '+')
					throw forInputString(s);

				if (len == 1) {
					throw forInputString(s);
				}
				i++;
			}
			multmin = limit / radix;
			while (i < len) {
				digit = digitMap.get(s.charAt(i++));
				if (digit == null) {
					throw forInputString(s);
				}
				if (digit < 0) {
					throw forInputString(s);
				}
				if (result < multmin) {
					throw forInputString(s);
				}
				result *= radix;
				if (result < limit + digit) {
					throw forInputString(s);
				}
				result -= digit;
			}
		} else {
			throw forInputString(s);
		}
		return negative ? result : -result;
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Numbers.toString(hi | (val & (hi - 1)), Numbers.MAX_RADIX)
				.substring(1);
	}

	/**
	 * ��62���ƣ���ĸ�����֣�����19λUUID����̵�UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
		sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
		sb.append(digits(uuid.getMostSignificantBits(), 4));
		sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
		sb.append(digits(uuid.getLeastSignificantBits(), 12));
		return sb.toString();
	}

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();

	}

	public static void main(String[] args) {
		long a = System.currentTimeMillis();
		// uuid();
//		 System.out.println(uuid());
//		System.out.println(generateShortUuid());
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < 999999; i++) {
////			set.add(uuid());
			set.add(generateShortUuid());
		}
		System.out.println(System.currentTimeMillis() - a);
		System.out.println(set.size());
	}
}
