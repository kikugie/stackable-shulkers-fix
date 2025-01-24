package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
	@WrapOperation(
		method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getMaxCount(Lnet/minecraft/item/ItemStack;)I")
	)
	private static int fixOverstackedSignalStrength(Inventory instance, ItemStack stack, Operation<Integer> original) {
		if (!Util.isWrapped(instance)) return original.call(instance, stack);
		return Util.determineSignalStrengthContribution(stack);
	}

	@ModifyReturnValue(
		method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I",
		at = @At("RETURN")
	)
	private static int capComparatorSignalStrength(int original) {
		return Util.limitComparatorOutput(original);
	}
}