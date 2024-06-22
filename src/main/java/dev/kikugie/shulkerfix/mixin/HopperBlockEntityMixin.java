package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @WrapOperation(method = "isFull", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private int modifyShulkerMaxCount(ItemStack instance, Operation<Integer> original) {
        return Util.isShulkerBox(instance) ? instance.getCount() : original.call(instance);
    }

    @WrapOperation(method = "isInventoryFull", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int modifyShulkerMaxCountStatic(ItemStack instance, Operation<Integer> original) {
        return Util.isShulkerBox(instance) ? 1 : original.call(instance);
    }

    @Inject(method = "canMergeItems", at = @At("HEAD"), cancellable = true)
    private static void cancelItemMerging(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
        if (Util.isShulkerBox(first) || Util.isShulkerBox(second)) cir.setReturnValue(false);
    }
}