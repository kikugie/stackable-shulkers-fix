package dev.kikugie.shulkerfix.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.shulkerfix.Util;
import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(LithiumStackList.class)
public class LithiumStackListMixin {
	@WrapOperation(
		method = "calculateSignalStrength",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I")
	)
	private int get1(ItemStack instance, Operation<Integer> original) {
		return Util.isShulkerBoxLimited(instance) ? 1 : original.call(instance);
	}
}
