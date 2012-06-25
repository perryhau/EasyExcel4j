package com.taobao.common.easyExcel4j.util;

import org.apache.commons.lang.StringUtils;

public class CamelCase {

	/**
	 * ���ַ���ת����camel case��
	 * <p>
	 * ����ַ�����<code>null</code>�򷵻�<code>null</code>��
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
	 * �˷����ᱣ�������»��ߺͿհ���������зָ�����
	 * </p>
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return camel case�ַ��������ԭ�ַ���Ϊ<code>null</code>���򷵻�<code>null</code>
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
	 * �����������﷨�����ɵ�<code>SENTENCE</code>��
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
		 * Parse sentence��
		 */
		public String parse(String str) {
			if (StringUtils.isEmpty(str)) {
				return str;
			}

			int length = str.length();
			StringBuilder buffer = new StringBuilder(length);

			for (int index = 0; index < length; index++) {
				char ch = str.charAt(index);

				// ���Կհס�
				if (Character.isWhitespace(ch)) {
					continue;
				}

				// ��д��ĸ��ʼ��UpperCaseWord����TitleCaseWord��
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

					// 1. wordIndex == length��˵�����һ����ĸΪ��д����upperCaseWord����֮��
					// 2. wordIndex == index��˵��index��Ϊһ��titleCaseWord��
					// 3. wordIndex > index��˵��index��wordIndex -
					// 1��ȫ���Ǵ�д����upperCaseWord����
					if (wordIndex == length || wordIndex > index) {
						index = parseUpperCaseWord(buffer, str, index, wordIndex);
					} else {
						index = parseTitleCaseWord(buffer, str, index);
					}

					continue;
				}

				// Сд��ĸ��ʼ��LowerCaseWord��
				if (Character.isLowerCase(ch)) {
					index = parseLowerCaseWord(buffer, str, index);
					continue;
				}

				// ���ֿ�ʼ��DigitWord��
				if (Character.isDigit(ch)) {
					index = parseDigitWord(buffer, str, index);
					continue;
				}

				// ����ĸ���ֿ�ʼ��Delimiter��
				inDelimiter(buffer, ch);
			}

			return buffer.toString();
		}

		private int parseUpperCaseWord(StringBuilder buffer, String str, int index, int length) {
			char ch = str.charAt(index++);

			// ����ĸ����Ȼ������Ϊ��д��
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// ������ĸ����ΪСд��
			for (; index < length; index++) {
				ch = str.charAt(index);
				inWord(buffer, ch);
			}

			return index - 1;
		}

		private int parseLowerCaseWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// ����ĸ����Ȼ������ΪСд��
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// ������ĸ����ΪСд��
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

			// ����ĸ����Ȼ������Ϊ��д��
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// ������ĸ����ΪСд��
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

			// ���ַ�����Ȼ������Ϊ���֡�
			if (buffer.length() == 0) {
				startDigitSentence(buffer, ch);
			} else {
				startDigitWord(buffer, ch);
			}

			// �����ַ�����Ϊ���֡�
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
