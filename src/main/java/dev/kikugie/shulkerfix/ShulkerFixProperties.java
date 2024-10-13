package dev.kikugie.shulkerfix;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.nio.file.Files;
import java.nio.file.Path;

public class ShulkerFixProperties {
	public boolean seenRuleNotification;

	public ShulkerFixProperties(boolean seenRuleNotification) {
		this.seenRuleNotification = seenRuleNotification;
	}

	public void save(MinecraftServer server) {
		if (seenRuleNotification) try {
			Path marker = server.getSavePath(WorldSavePath.ROOT).resolve("data/shulkerfix.marker");
			Files.createDirectories(marker.getParent());
			Files.createFile(marker);
			Files.writeString(marker, "\uD83C\uDF4C");
		} catch (Exception e) {
			ShulkerFixMod.LOGGER.warn("Failed to create marker file for shulkerfix", e);
		}
	}

	public static ShulkerFixProperties load(MinecraftServer server) {
		Path marker = server.getSavePath(WorldSavePath.ROOT).resolve("data/shulkerfix.marker");
		boolean marked = Files.exists(marker);
		return new ShulkerFixProperties(marked);
	}
}
