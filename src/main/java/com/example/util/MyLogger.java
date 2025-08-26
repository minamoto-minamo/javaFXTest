package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MyLogger {
	// region --- マスキング（最低限の実用パターン） ---
	private static final Pattern PWD = Pattern.compile("(?i)(password|pwd)\s*=?\s*([^&\\s]+)");
	private static final Pattern TOKEN = Pattern.compile("(?i)(authorization|bearer|access[-_ ]?token)\s*[:=]\\s*([^\\s]+)");
	private static final Pattern EMAIL = Pattern.compile("([A-Za-z0-9._%+-]{1,64})@([A-Za-z0-9.-]{1,255})");
	private static final Pattern CARD = Pattern.compile("(?<!\\d)(?:\\d[ -]?){13,19}(?!\\d)");
	private final Logger log;

	private MyLogger(Class<?> type) {
		this.log = LoggerFactory.getLogger(type);
	}

	public static MyLogger get(Class<?> type) {
		return new MyLogger(type);
	}

	public static String causeChain(Throwable t) {
		final StringBuilder sb = new StringBuilder(128);
		Throwable cur = t;
		while (cur != null) {
			if (!sb.isEmpty()) sb.append(" <- ");
			sb.append(cur.getClass().getSimpleName()).append(": ").append(cur.getMessage());
			cur = cur.getCause();
		}
		return sb.toString();
	}

	private static Object[] sanitize(Object[] args) {
		if (args == null || args.length == 0) return args;
		final Object[] out = Arrays.copyOf(args, args.length);
		for (int i = 0; i < out.length; i++) {
			final Object a = out[i];
			if (a == null) continue;
			final String s = String.valueOf(a);
			out[i] = mask(s);
		}
		return out;
	}

	private static Object[] append(Throwable t, Object[] args) {
		final Object[] sanitized = sanitize(args);
		final Object[] out = Arrays.copyOf(sanitized, sanitized.length + 1);
		out[out.length - 1] = t;
		return out;
	}

	private static String mask(String s) {
		String x = s;
		x = replaceGroupKeepingKey(PWD, x, "****");
		x = replaceGroupKeepingKey(TOKEN, x, "****");
		x = maskEmail(x);
		x = maskCard(x);
		return x;
	}

	private static String replaceGroupKeepingKey(Pattern p, String s, String replacement) {
		final Matcher m = p.matcher(s);
		final StringBuilder sb = new StringBuilder();
		while (m.find()) {
			final String key = m.group(1);
			m.appendReplacement(sb, Matcher.quoteReplacement(key + "=" + replacement));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static String maskEmail(String s) {
		final Matcher m = EMAIL.matcher(s);
		final StringBuilder sb = new StringBuilder();
		while (m.find()) {
			final String local = m.group(1);
			final String domain = m.group(2);
			final String masked = (local.length() <= 2) ? "*" : local.substring(0, 2) + "***";
			m.appendReplacement(sb, Matcher.quoteReplacement(masked + "@" + domain));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static String maskCard(String s) {
		final Matcher m = CARD.matcher(s);
		final StringBuilder sb = new StringBuilder();
		while (m.find()) {
			final String digits = m.group().replace(" ", "").replace("-", "");
			if (digits.length() < 13 || digits.length() > 19) continue;
			final String masked = digits.substring(0, 6) + repeat('*', Math.max(0, digits.length() - 10)) + digits.substring(digits.length() - 4);
			m.appendReplacement(sb, Matcher.quoteReplacement(masked));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static String repeat(char c, int n) {
		final char[] cs = new char[n];
		Arrays.fill(cs, c);
		return new String(cs);
	}

	public static String fmt(String kv, Object val) {
		// 軽量な key=value 生成（値はマスク反映）
		return kv + "=" + mask(String.valueOf(val));
	}

	public void trace(String msg, Object... args) {
		if (log.isTraceEnabled()) log.trace(msg, sanitize(args));
	}

	public void debug(String msg, Object... args) {
		if (log.isDebugEnabled()) log.debug(msg, sanitize(args));
	}

	public void info(String msg, Object... args) {
		if (log.isInfoEnabled()) log.info(msg, sanitize(args));
	}

	public void warn(String msg, Object... args) {
		if (log.isWarnEnabled()) log.warn(msg, sanitize(args));
	}

	public void error(String msg, Object... args) {
		if (log.isErrorEnabled()) log.error(msg, sanitize(args));
	}

	public void trace(Throwable t, String msg, Object... args) {
		if (log.isTraceEnabled()) log.trace(msg, append(t, args));
	}

	public void debug(Throwable t, String msg, Object... args) {
		if (log.isDebugEnabled()) log.debug(msg, append(t, args));
	}

	public void info(Throwable t, String msg, Object... args) {
		if (log.isInfoEnabled()) log.info(msg, append(t, args));
	}

	public void warn(Throwable t, String msg, Object... args) {
		if (log.isWarnEnabled()) log.warn(msg, append(t, args));
	}

	public void error(Throwable t, String msg, Object... args) {
		if (log.isErrorEnabled()) log.error(msg, append(t, args));
	}


	/**
	 * 例外の全文スタックと原因チェーン要約を二段で出す
	 */
	public void errorFull(Throwable t, String msg, Object... args) {
		error(t, msg + " | CAUSES=" + causeChain(t), args);
	}
}
