package dev.kikugie.shulkerfix;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class Util {
	public static boolean isShulkerBox(ItemStack stack) {
		return stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
	}

	public static boolean hasCustomMaxStackSize(ItemStack stack) {
		int defaultStackSize = stack.getDefaultComponents().getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1);
		int currentStackSize = stack.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1);
		return defaultStackSize != currentStackSize;
	}
}