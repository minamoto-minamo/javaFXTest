package com.example.ui.service.base;

import com.example.ui.service.LoadPageService;
import com.example.ui.service.LogMessageService;
import com.example.ui.util.HtmlException;

public class HtmlServiceFactory {
	public static HtmlService create(Object[] args) throws HtmlException {
		return switch (args[0].toString()) {
			case LoadPageService.action -> new LoadPageService(args);
			case LogMessageService.action -> new LogMessageService(args);
			default -> throw new HtmlException("不明な動作タイプを検出しました。");
		};
	}
}
