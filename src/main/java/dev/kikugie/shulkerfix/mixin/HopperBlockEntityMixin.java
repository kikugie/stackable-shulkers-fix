package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
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

	/*
	@WrapOperation(
		method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;")
	)
	private static ItemStack modifyShulkerCount(Inventory from, Inventory to, ItemStack stack, Direction side, Operation<ItemStack> original) {
		if (!ShulkerFixSettings.hopperCollectSingleShulkers || !Util.isShulkerBox(stack) || stack.getCount() <= 1)
			return original.call(from, to, stack, side);
		if (original.call(from, to, stack.copyWithCount(1), side).isEmpty())
			stack.decrement(1);
		return stack;
	}

	@Inject(
		method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"),
		cancellable = true
	)
	private static void checkIsShulker(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) ItemStack transferredStack) {
		ItemStack stack = itemEntity.getStack();
		if (ShulkerFixSettings.hopperCollectSingleShulkers && Util.isShulkerBox(stack) && stack.getCount() > 1) {
			itemEntity.setStack(transferredStack);
			cir.setReturnValue(true);
		}
	}
	 */

	@WrapOperation(
		method = "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z")
	)
	private static boolean limitMinecartCollectItems(Inventory inventory, ItemEntity itemEntity, Operation<Boolean> original) {
		if (!Util.isShulkerBox(itemEntity.getStack())) return original.call(inventory, itemEntity);
		if (ShulkerFixSettings.hopperCollectSingleShulkers && inventory instanceof HopperBlockEntity)
			return Util.collectOneItem(inventory, itemEntity);
		else if (ShulkerFixSettings.minecartCollectSingleShulkers && inventory instanceof HopperMinecartEntity)
			return Util.collectOneItem(inventory, itemEntity);
		else return original.call(inventory, itemEntity);
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