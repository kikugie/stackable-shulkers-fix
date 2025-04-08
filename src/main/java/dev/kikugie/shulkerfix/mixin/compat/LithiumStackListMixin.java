package dev.kikugie.shulkerfix.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import net.caffeinemc.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Prevents shulker stacking with Lithium installed.
 */
@Pseudo
@Mixin(LithiumStackList.class)
public class LithiumStackListMixin {
	@WrapOperation(
		method = "calculateSignalStrength",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private int modifyShulkerMaxCount(ItemStack instance, Operation<Integer> original) {
		return Util.determineSignalStrengthContribution(instance);
	}

	@ModifyReturnValue(
		method = "calculateSignalStrength",
		at = @At("RETURN"),
		remap = false
	)
	private int capComparatorSignalStrength(int original) {
		return Util.limitComparatorOutput(original);
	}
}
