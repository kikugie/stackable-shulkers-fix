package dev.kikugie.shulkerfix;

import dev.kikugie.shulkerfix.carpet.ShulkerFixExtension;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShulkerFixMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(ShulkerFixMod.class);
	public static final String MOD_ID = "shulkerfix";

	@Override
	public void onInitialize() {
		ShulkerFixExtension.init();
	}
}
