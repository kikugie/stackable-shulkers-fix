package dev.kikugie.shulkerfix.mixin.compat;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.SettingsManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = SettingsManager.class, remap = false)
public interface CarpetSettingsManagerAccessor {
	@Invoker
	@SuppressWarnings("UnusedReturnValue")
	int invokeSetDefault(ServerCommandSource source, CarpetRule<?> rule, String stringValue);
}
