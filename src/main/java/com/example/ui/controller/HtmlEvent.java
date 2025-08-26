package com.example.ui.controller;

import com.example.ui.service.base.HtmlService;
import com.example.ui.service.base.HtmlServiceFactory;
import com.example.ui.util.HtmlException;
import com.example.ui.view.WebEngineManager;
import netscape.javascript.JSObject;

import com.example.util.MyLogger; // ← 追加

public class HtmlEvent {

	private static final MyLogger LOG = MyLogger.get(HtmlEvent.class); // ← 追加

	public void call(Object arg) {
		final Object[] args = convertToArray(arg);

		try {
			LOG.debug("call start {}", MyLogger.fmt("args", java.util.Arrays.toString(args)));
			HtmlService service = HtmlServiceFactory.create(args);
			service.run();
			LOG.debug("call end");
		} catch (HtmlException e) {
			LOG.errorFull(e, "HTML処理に失敗しました {}", MyLogger.fmt("args", java.util.Arrays.toString(args)));
			handleError(e.getMessage() != null ? e.getMessage() : "不明なエラーが発生しました。");
		} catch (Throwable t) {
			// 想定外エラーも握りつぶさずログ
			LOG.errorFull(t, "想定外エラー {}", MyLogger.fmt("args", java.util.Arrays.toString(args)));
			handleError("不明なエラーが発生しました。");
		}
	}

	private Object[] convertToArray(Object arg) {
		if (!(arg instanceof JSObject jsObj)) {
			return new Object[]{arg};
		}

		try {
			int length = ((Number) jsObj.getMember("length")).intValue();
			Object[] arr = new Object[length];
			for (int i = 0; i < length; i++) {
				arr[i] = jsObj.getSlot(i);
			}
			return arr;
		} catch (Exception e) {
			LOG.warn("JSObject is not array-like: {}", e.toString()); // ここも printStackTrace を置換
			return new Object[]{arg};
		}
	}

	private void handleError(String msg) {
		String safe = (msg == null) ? "" : msg;
		String escapedMsg = safe.replace("\"", "\\\""); // "
		String js = "window.alertMessage(\"" + escapedMsg + "\")";
		WebEngineManager.getInstance().getEngine().executeScript(js);
	}
}
