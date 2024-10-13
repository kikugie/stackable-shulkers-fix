package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HopperMinecartEntity.class)
public class HopperMinecartEntityMixin {
    @WrapOperation(
        method = "canOperate",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z")
    )
    private boolean limitCollectItems(Inventory inventory, ItemEntity itemEntity, Operation<Boolean> original) {
        return Util.isShulkerBox(itemEntity.getStack()) && ShulkerFixSettings.minecartCollectSingleShulkers ? Util.collectOneItem(inventory, itemEntity) : original.call(inventory, itemEntity);
    }
}
