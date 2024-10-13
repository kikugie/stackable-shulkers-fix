package dev.kikugie.shulkerfix.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.kikugie.shulkerfix.ShulkerFixMod;
import dev.kikugie.shulkerfix.ShulkerFixProperties;
import dev.kikugie.shulkerfix.mixin.compat.CarpetSettingsManagerAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

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
	public void onServerLoadedWorlds(MinecraftServer server) {
		ShulkerFixProperties properties = ShulkerFixProperties.load(server);
		if (!properties.seenRuleNotification) try {
			ShulkerFixSettings.hopperShulkerStacking = false;
			ShulkerFixSettings.overstackedShulkerSignalStrength = true;

			SettingsManager manager = CarpetServer.settingsManager;
			((CarpetSettingsManagerAccessor) manager).invokeSetDefault(
				server.getCommandSource(),
				manager.getCarpetRule("hopperShulkerStacking"),
				"false");
			((CarpetSettingsManagerAccessor) manager).invokeSetDefault(
				server.getCommandSource(),
				manager.getCarpetRule("overstackedShulkerSignalStrength"),
				"true");
			properties.seenRuleNotification = true;
		} catch (Exception e) {
			ShulkerFixMod.LOGGER.warn("Failed to save rule defaults", e);
		}
		properties.save(server);
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
