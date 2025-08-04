package com.example.ui.view;

import com.example.ui.controller.HtmlEvent;
import com.example.ui.resource.ResourceLoader;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public final class WebEngineManager {
	private static WebEngineManager instance;
	private final WebEngine engine;
	private final HtmlEvent bridge = new HtmlEvent();

	private WebEngineManager(WebEngine engine) {
		this.engine = engine;
		this.engine.load(ResourceLoader.getHTMLUrl("main")); // 初期ページ
		this.engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
			if (newDoc != null) {
				JSObject window = (JSObject) this.engine.executeScript("window");
				window.setMember("javaConnector", bridge);
			}
		});

		this.engine.setOnAlert(event -> System.out.println("[ALERT] " + event.getData()));

		this.engine.setOnError(event -> System.out.println("[ERROR] " + event.getMessage()));
	}

	public static void initialize(WebEngine engine) {
		if (instance != null) throw new IllegalStateException("WebEngineManager is already initialized.");
		instance = new WebEngineManager(engine);
	}

	public static WebEngineManager getInstance() {
		if (instance == null) throw new IllegalStateException("WebEngineManager is not initialized.");
		return instance;
	}

	public WebEngine getEngine() {
		return engine;
	}

	public void loadPage(String name) {
		engine.load(ResourceLoader.getHTMLUrl(name));
	}

}
