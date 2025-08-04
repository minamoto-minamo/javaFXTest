package com.example.config;


import com.example.ui.resource.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public final class AppSettings {
	private static final AppSettings HOLDER = load();
	public WindowConfig window;

	private AppSettings() {
	}

	private static AppSettings load() {
		try (InputStream in = ResourceLoader.getConfigYml()) {
			return new Yaml().loadAs(in, AppSettings.class);
		} catch (Exception e) {
			throw new RuntimeException("設定ファイル読み込み失敗", e);
		}
	}

	public static AppSettings getInstance() {
		return HOLDER;
	}

	public static class WindowConfig {
		public String title;
		public Size size;
	}

	public static class Size {
		public int width;
		public int height;
	}
}