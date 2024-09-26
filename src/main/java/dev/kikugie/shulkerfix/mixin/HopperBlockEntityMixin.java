package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
	@WrapOperation(
		method = "isFull",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private int modifyShulkerMaxCount(ItemStack instance, Operation<Integer> original) {
		return Util.isShulkerBox(instance) ? instance.getCount() : original.call(instance);
	}

	@WrapOperation(
		method = "isInventoryFull",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private static int modifyShulkerMaxCountStatic(ItemStack instance, Operation<Integer> original) {
		return Util.isShulkerBox(instance) ? 1 : original.call(instance);
	}

	@WrapOperation(
		method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;")
	)
	private static ItemStack modifyShulkerCount(Inventory from, Inventory to, ItemStack stack, Direction side, Operation<ItemStack> original) {
		if (ShulkerFixSettings.minecartOnlyTransferOneShulker && Util.isShulkerBox(stack) && stack.getCount() > 1) {
			if (original.call(from, to, stack.copyWithCount(1), side).isEmpty())
				stack.decrement(1);
			return stack;
		} else return original.call(from, to, stack, side);
    }

	@Inject(
		method = "canMergeItems",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void cancelItemMerging(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
		if (Util.isShulkerBox(first) || Util.isShulkerBox(second)) cir.setReturnValue(false);
	}
}