package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements {@link ShulkerFixSettings#legacyShulkerItemMerging}.
 */
@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Shadow
	private static void merge(ItemEntity targetEntity, ItemStack targetStack, ItemEntity sourceEntity, ItemStack sourceStack) {
	}

	@Inject(
		method = "tryMerge(Lnet/minecraft/entity/ItemEntity;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;canMerge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"),
		cancellable = true
	)
	private void applyLegacyMerge(ItemEntity other, CallbackInfo ci, @Local(ordinal = 0) ItemStack selfStack, @Local(ordinal = 1) ItemStack otherStack) {
		if (!ShulkerFixSettings.legacyShulkerItemMerging || selfStack.getMaxCount() == 1 || !Util.isShulkerBox(selfStack)) return;

		ItemEntity self = (ItemEntity) (Object) this;
		// Normally, items would only merge if their combined count is less or equals the max count.
		// Carpet used to ignore this behaviour, and so do we.
		// Entity owners are checked before invoking this mixin.
		if (selfStack.getCount() < selfStack.getMaxCount() && ItemStack.areItemsAndComponentsEqual(selfStack, otherStack)) {
			// Carpet modified the 'Entity.age' instead of 'ItemEntity.itemAge',
			// which was actively used by various contraptions.
			self.age = Math.min(self.age, other.age);
			merge(self, selfStack, other, otherStack);
			ci.cancel();
		}
	}
}
