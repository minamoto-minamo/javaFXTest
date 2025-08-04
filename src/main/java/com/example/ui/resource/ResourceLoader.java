package com.example.ui.resource;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class ResourceLoader {
	public static Image getIcon(){
		return new Image(Objects.requireNonNull(ResourceLoader.class.getResourceAsStream("/icon/icon.png")));
	}

	public static String getHTMLUrl(String name) {
		String path = "/web/html/" + name + ".html";
		URL resource = ResourceLoader.class.getResource(path);
		if (resource == null) {
			throw new IllegalArgumentException("ページが見つかりません: " + name);
		}
		return resource.toExternalForm();
	}


	public static InputStream getConfigYml(){
		return Objects.requireNonNull(ResourceLoader.class.getResourceAsStream("/config/app-config.yml"));
	}

}
