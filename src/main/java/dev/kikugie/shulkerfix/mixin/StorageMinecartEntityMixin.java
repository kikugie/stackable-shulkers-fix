package dev.kikugie.shulkerfix.mixin;

import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Provides two method qualifiers for 1.21.3 compatibility.
 */
@Mixin(StorageMinecartEntity.class)
public class StorageMinecartEntityMixin {
	@SuppressWarnings({"UnresolvedMixinReference", "UnnecessaryQualifiedMemberReference"})
	@ModifyArg(
		method = {
			"Lnet/minecraft/entity/vehicle/StorageMinecartEntity;applySlowdown()V", // <1.21.2
			"Lnet/minecraft/entity/vehicle/StorageMinecartEntity;applySlowdown(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"
		},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/screen/ScreenHandler;calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I"
		),
		require = 1
	)
	private Inventory fixMinecartSlowdown(Inventory instance) {
		return ShulkerFixSettings.overstackedMinecartSlowdown ? instance : Util.wrapInventory(instance);
	}
}
