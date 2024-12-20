package dev.kikugie.shulkerfix.mixin;

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
		return !Util.isShulkerBoxLimited(stack) || Util.isWrapped(instance) ? original.call(instance, stack) : 1;
	}
}