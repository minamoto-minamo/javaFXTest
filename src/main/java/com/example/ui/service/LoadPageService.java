package com.example.ui.service;

import com.example.ui.service.base.HtmlService;
import com.example.ui.util.HtmlException;
import com.example.ui.view.WebEngineManager;

public class LoadPageService implements HtmlService {

	public static final String action = "loadPage";
	private final String pageName;

	public LoadPageService(String pageName){
		this.pageName =pageName;
	}

	@Override
	public void run() throws HtmlException {
		try{
			WebEngineManager.getInstance().loadPage(pageName);
		} catch (IllegalArgumentException e) {
			throw new HtmlException("呼びだし先のページが見つかりません");
		} catch (Exception e) {
			throw new HtmlException("ページの呼び出し時に不明なエラーが発生しました。", e);
		}
	}
}
