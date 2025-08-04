package com.example.ui.service;

import com.example.ui.service.base.HtmlService;
import com.example.ui.util.HtmlException;

public class LogMessageService  implements HtmlService {

	public static final String action = "logMsg";
	private final String message;

	public LogMessageService(Object[] args) {
		message = args[1].toString();
	}

	@Override
	public void run() throws HtmlException {
		System.out.println("JSから受信: " + message);
	}
}
