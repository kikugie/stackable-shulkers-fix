package dev.kikugie.shulkerfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.shulkerfix.Util;
import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Implementation from the <a href="https://github.com/ch-yx/fabric-carpet/blob/18d4f8fdc02069fb4d41cd28d703f8d7a6aa7579/src/main/java/carpet/mixins/ItemStack_stackableShulkerBoxesEncodeMixin.java">ch-yx fork</a>.
 */
@Pseudo
@Mixin(targets = "net.minecraft.item.ItemStack$1")
public class ItemStackCodecMixin {
	@Unique
	private static final String MARKER = "__carpet_data!!the_max_stack_size_tag_is_fake!__";

	@ModifyVariable(method = "encode(Lnet/minecraft/network/RegistryByteBuf;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), argsOnly = true)
	private ItemStack setModifiedStackSize(ItemStack stack) {
		if (ShulkerFixSettings.clientShulkerSync &&
			stack.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1) < stack.getCount() &&
			!stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).contains(MARKER) &&
			!Util.hasCustomMaxStackSize(stack)
		) {
			stack = stack.copy();
			stack.set(DataComponentTypes.MAX_STACK_SIZE, stack.getMaxCount());
			NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> nbt.putBoolean(MARKER, true));
		}
		return stack;
	}

	@ModifyReturnValue(method = "decode(Lnet/minecraft/network/RegistryByteBuf;)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
	private ItemStack getModifiedStackSize(ItemStack stack) {
		// We still do decoding, in case the rule was disabled
		if (stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).contains(MARKER)) {
			stack = stack.copy();
			stack.set(DataComponentTypes.MAX_STACK_SIZE, stack.getDefaultComponents().get(DataComponentTypes.MAX_STACK_SIZE));
			stack.capCount(stack.getMaxCount());
			NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> nbt.remove(MARKER));
		}
		return stack;
	}
}
