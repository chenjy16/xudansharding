package com.midea.trade.sharding.jdbc.results.merger;

/**
 * MergeUtils
 */
public class MergeUtils {

	public static String getScriptExp(String exp) {
		exp = exp.replaceAll("AND", "&&");
		exp = exp.replaceAll("OR", "||");
		StringBuilder sb = new StringBuilder();
		char[] chars = exp.toCharArray();
		for (int i = 0; i < chars.length; i++) {

			char c = chars[i];
			switch (c) {
			case '<':
				char pre = chars[i - 1],
				after = chars[i + 1];
				int j = i + 1;
				while (after == ' ') {// 排除空格
					after = chars[++j];
				}
				if (after == '>') {
					i = j;
					sb.append("!=");
					continue;
				}
				sb.append(c);
				break;
			case '=':
				pre = chars[i - 1];
				after = chars[i + 1];
				j = i - 1;
				while (pre == ' ') {
					pre = chars[--j];
				}
				if (pre == '>' || pre == '<') {// 排除空格,< =
					sb.append(c);
					continue;
				}
				sb.append(c);
				sb.append(c);// append 变成＝＝
				break;
			default:
				sb.append(c);

			}

		}
		return sb.toString();
	}

}
