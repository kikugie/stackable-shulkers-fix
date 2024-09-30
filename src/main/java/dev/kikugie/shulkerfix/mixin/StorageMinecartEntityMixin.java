package dev.kikugie.shulkerfix.mixin;

import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StorageMinecartEntity.class)
public class StorageMinecartEntityMixin {
	@ModifyArg(method = "applySlowdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I"))
	private Inventory fixMinecartSlowdown(Inventory instance) {
		return ShulkerFixSettings.overstackedMinecartSlowdown ? instance : Util.wrapInventory(instance);
	}
}
