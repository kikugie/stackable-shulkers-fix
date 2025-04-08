package dev.kikugie.shulkerfix.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import net.caffeinemc.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Prevents shulker stacking with Lithium installed.
 * <br>
 * <li>{@code me.jellysquid.mods.lithium.common.hopper.HopperHelper} is used for Lithium <0.14 compatibility.</li>
 * <li>{@code tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z} is used for Lithium <0.13 compatibility.</li>
 */
@Pseudo
@Mixin(HopperHelper.class)
public class LithiumHopperHelperMixin {
	@WrapOperation(
		method = {
			"determineComparatorUpdatePattern",
			"tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z"
		},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private static int modifyShulkerMaxCount(ItemStack instance, Operation<Integer> original) {
		return Util.determineSignalStrengthContribution(instance);
	}
}