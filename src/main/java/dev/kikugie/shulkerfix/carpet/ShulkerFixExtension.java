package dev.kikugie.shulkerfix.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.kikugie.shulkerfix.ShulkerFixMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;

public class ShulkerFixExtension implements CarpetExtension {
	public static final ShulkerFixExtension INSTANCE = new ShulkerFixExtension();

	public static void init() {
		CarpetServer.manageExtension(INSTANCE);
	}

	@Override
	public String version() {
		var container = FabricLoader.getInstance().getModContainer(ShulkerFixMod.MOD_ID);
		return container.isPresent() ? container.get().getMetadata().getVersion().getFriendlyString() : "undefined";
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(ShulkerFixSettings.class);
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		try (InputStream stream = ShulkerFixSettings.class.getClassLoader().getResourceAsStream("assets/shulkerfix/lang/%s.json".formatted(lang))) {
			if (stream == null) return Collections.emptyMap();
			Gson gson = new Gson();
			TypeToken<Map<String, String>> token = new TypeToken<>() {
			};
			Reader reader = new InputStreamReader(stream);
			return gson.fromJson(reader, token.getType());
		} catch (IOException e) {
			ShulkerFixMod.LOGGER.error("Failed to decode mod language file %s.json".formatted(lang), e);
			return Collections.emptyMap();
		}
	}
}
