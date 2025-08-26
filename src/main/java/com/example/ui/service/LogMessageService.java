package com.example.ui.service;

import com.example.ui.service.base.HtmlService;
import com.example.ui.util.HtmlException;

public class LogMessageService implements HtmlService {

	public static final String action = "logMsg";
	private final String message;

	public LogMessageService(String message) {
		this.message = message;
	}

	@Override
	public void run() throws HtmlException {
		try {
			System.out.println("JSから受信: " + message);
		} catch (Exception e) {
			throw new HtmlException("メッセージの出力時にエラーが発生しました。", e);
		}
	}
}
