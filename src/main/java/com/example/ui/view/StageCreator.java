package com.example.ui.view;

import com.example.config.AppSettings;
import com.example.ui.resource.ResourceLoader;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class StageCreator {
	public static void awake(Stage stage, WebView webView) {
		AppSettings settings = AppSettings.getInstance();

		Scene scene = new Scene(webView, settings.window.size.width, settings.window.size.height);

		stage.setScene(scene);
		stage.setTitle(settings.window.title);
		stage.getIcons().add(ResourceLoader.getIcon());
		stage.setResizable(false);
		stage.show();
	}
}
