package com.taobao.common.easyExcel4j.util;

import org.apache.commons.lang.StringUtils;

public class CamelCase {

	/**
	 * 将字符串转换成camel case。
	 * <p>
	 * 如果字符串是<code>null</code>则返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.toCamelCase(null)  = null
	 * StringUtil.toCamelCase("")    = ""
	 * StringUtil.toCamelCase("aBc") = "aBc"
	 * StringUtil.toCamelCase("aBc def") = "aBcDef"
	 * StringUtil.toCamelCase("aBc def_ghi") = "aBcDefGhi"
	 * StringUtil.toCamelCase("aBc def_ghi 123") = "aBcDefGhi123"
	 * </pre>
	 * 
	 * </p>
	 * <p>
	 * 此方法会保留除了下划线和空白以外的所有分隔符。
	 * </p>
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return camel case字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
	 */
	public static String valueOf(String str) {
		return new WordTokenizer() {

			@Override
			protected void startSentence(StringBuilder buffer, char ch) {
				buffer.append(Character.toLowerCase(ch));
			}

			@Override
			protected void startWord(StringBuilder buffer, char ch) {
				if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
					buffer.append(Character.toUpperCase(ch));
				} else {
					buffer.append(Character.toLowerCase(ch));
				}
			}

			@Override
			protected void inWord(StringBuilder buffer, char ch) {
				buffer.append(Character.toLowerCase(ch));
			}

			@Override
			protected void startDigitSentence(StringBuilder buffer, char ch) {
				buffer.append(ch);
			}

			@Override
			protected void startDigitWord(StringBuilder buffer, char ch) {
				buffer.append(ch);
			}

			@Override
			protected void inDigitWord(StringBuilder buffer, char ch) {
				buffer.append(ch);
			}

			@Override
			protected void inDelimiter(StringBuilder buffer, char ch) {
				if (ch != UNDERSCORE) {
					buffer.append(ch);
				}
			}
		}.parse(str);
	}

	/**
	 * 解析出下列语法所构成的<code>SENTENCE</code>。
	 * 
	 * <pre>
	 *  SENTENCE = WORD (DELIMITER* WORD)*
	 * 
	 *  WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
	 * 
	 *  UPPER_CASE_WORD = UPPER_CASE_LETTER+
	 *  LOWER_CASE_WORD = LOWER_CASE_LETTER+
	 *  TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
	 *  DIGIT_WORD      = DIGIT+
	 * 
	 *  UPPER_CASE_LETTER = Character.isUpperCase()
	 *  LOWER_CASE_LETTER = Character.isLowerCase()
	 *  DIGIT             = Character.isDigit()
	 *  NON_LETTER_DIGIT  = !Character.isUpperCase() && !Character.isLowerCase() && !Character.isDigit()
	 * 
	 *  DELIMITER = WHITESPACE | NON_LETTER_DIGIT
	 * </pre>
	 */
	private abstract static class WordTokenizer {
		protected static final char UNDERSCORE = '_';

		/**
		 * Parse sentence。
		 */
		public String parse(String str) {
			if (StringUtils.isEmpty(str)) {
				return str;
			}

			int length = str.length();
			StringBuilder buffer = new StringBuilder(length);

			for (int index = 0; index < length; index++) {
				char ch = str.charAt(index);

				// 忽略空白。
				if (Character.isWhitespace(ch)) {
					continue;
				}

				// 大写字母开始：UpperCaseWord或是TitleCaseWord。
				if (Character.isUpperCase(ch)) {
					int wordIndex = index + 1;

					while (wordIndex < length) {
						char wordChar = str.charAt(wordIndex);

						if (Character.isUpperCase(wordChar)) {
							wordIndex++;
						} else if (Character.isLowerCase(wordChar)) {
							wordIndex--;
							break;
						} else {
							break;
						}
					}

					// 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
					// 2. wordIndex == index，说明index处为一个titleCaseWord。
					// 3. wordIndex > index，说明index到wordIndex -
					// 1处全部是大写，以upperCaseWord处理。
					if (wordIndex == length || wordIndex > index) {
						index = parseUpperCaseWord(buffer, str, index, wordIndex);
					} else {
						index = parseTitleCaseWord(buffer, str, index);
					}

					continue;
				}

				// 小写字母开始：LowerCaseWord。
				if (Character.isLowerCase(ch)) {
					index = parseLowerCaseWord(buffer, str, index);
					continue;
				}

				// 数字开始：DigitWord。
				if (Character.isDigit(ch)) {
					index = parseDigitWord(buffer, str, index);
					continue;
				}

				// 非字母数字开始：Delimiter。
				inDelimiter(buffer, ch);
			}

			return buffer.toString();
		}

		private int parseUpperCaseWord(StringBuilder buffer, String str, int index, int length) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为大写。
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// 后续字母，必为小写。
			for (; index < length; index++) {
				ch = str.charAt(index);
				inWord(buffer, ch);
			}

			return index - 1;
		}

		private int parseLowerCaseWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为小写。
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// 后续字母，必为小写。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isLowerCase(ch)) {
					inWord(buffer, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		private int parseTitleCaseWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为大写。
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// 后续字母，必为小写。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isLowerCase(ch)) {
					inWord(buffer, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		private int parseDigitWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// 首字符，必然存在且为数字。
			if (buffer.length() == 0) {
				startDigitSentence(buffer, ch);
			} else {
				startDigitWord(buffer, ch);
			}

			// 后续字符，必为数字。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isDigit(ch)) {
					inDigitWord(buffer, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		protected boolean isDelimiter(char ch) {
			return !Character.isUpperCase(ch) && !Character.isLowerCase(ch) && !Character.isDigit(ch);
		}

		protected abstract void startSentence(StringBuilder buffer, char ch);

		protected abstract void startWord(StringBuilder buffer, char ch);

		protected abstract void inWord(StringBuilder buffer, char ch);

		protected abstract void startDigitSentence(StringBuilder buffer, char ch);

		protected abstract void startDigitWord(StringBuilder buffer, char ch);

		protected abstract void inDigitWord(StringBuilder buffer, char ch);

		protected abstract void inDelimiter(StringBuilder buffer, char ch);
	}

}
