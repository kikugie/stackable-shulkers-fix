package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = HopperHelper.class)
public class LithiumHopperHelperMixin {
	@SuppressWarnings("UnresolvedMixinReference")
	@WrapOperation(
		method = {
			"determineComparatorUpdatePattern",
			// Lithium <0.13 compat
			"tryMoveSingleItem(Lnet/minecraft/inventoryay/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z",
			"tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z"
		},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private static int modifyShulkerMaxCount(ItemStack instance, Operation<Integer> original) {
		return Util.isShulkerBox(instance) ? 1 : original.call(instance);
	}
}