package com.example.ui.controller;


import com.example.ui.service.base.HtmlService;
import com.example.ui.service.base.HtmlServiceFactory;
import com.example.ui.util.HtmlException;
import com.example.ui.view.WebEngineManager;
import netscape.javascript.JSObject;

public class HtmlEvent {
	public void call(Object arg) {
		Object[] args = convertToArray(arg);
		try {
			HtmlService service = HtmlServiceFactory.create(args);
			service.run();
		} catch (HtmlException e) {
			e.printStackTrace();
			handleError(e.getMessage());
		}
	}

	private Object[] convertToArray(Object arg) {
		if (!(arg instanceof JSObject jsObj)) {
			return new Object[]{arg};
		}

		try {
			int length = (int) jsObj.getMember("length");
			Object[] arr = new Object[length];
			for (int i = 0; i < length; i++) {
				arr[i] = jsObj.getSlot(i);
			}
			return arr;
		} catch (Exception e) {
			System.err.println("JSObject is not an array-like object.");
			return new Object[]{arg};
		}
	}

	private void handleError(String msg) {
		String escapedMsg = msg.replace("\"", "\\\""); // ダブルクォートをエスケープ
		String js = "window.alertMessage(\"" + escapedMsg + "\")";
		WebEngineManager.getInstance().getEngine().executeScript(js);
	}
}
