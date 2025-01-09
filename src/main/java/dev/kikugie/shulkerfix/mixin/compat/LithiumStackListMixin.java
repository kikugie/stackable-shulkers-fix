package dev.kikugie.shulkerfix.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Prevents shulker stacking with Lithium installed.
 * <br>
 * <li>{@code me.jellysquid.mods.lithium.common.hopper.HopperHelper} is used for Lithium <0.14 compatibility.</li>
 */
@Pseudo
@Mixin(targets = {"net.caffeinemc.mods.lithium.common.hopper.LithiumStackList", "me.jellysquid.mods.lithium.common.hopper.LithiumStackList"})
public class LithiumStackListMixin {
	@WrapOperation(
		method = "calculateSignalStrength",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private int modifyShulkerMaxCount(ItemStack instance, Operation<Integer> original) {
		return Util.isShulkerBoxLimited(instance) ? 1 : original.call(instance);
	}

	@ModifyReturnValue(
		method = "calculateSignalStrength",
		at = @At("RETURN")
	)
	private int capComparatorSignalStrength(int original) {
		return Util.limitComparatorOutput(original);
	}
}
