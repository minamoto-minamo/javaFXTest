package com.example;

import com.example.ui.view.StageCreator;
import com.example.ui.view.WebEngineManager;
import javafx.application.Application;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainApp extends Application {
	@Override
	public void start(Stage stage) {
		WebView webView = new WebView();
		WebEngine engine = webView.getEngine();
		WebEngineManager.initialize(engine);
		StageCreator.awake(stage,webView);
	}

	public static void main(String[] args) {
		launch(args);
	}
}