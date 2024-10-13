package dev.kikugie.shulkerfix;

import dev.kikugie.shulkerfix.carpet.ShulkerFixSettings;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class Util {
	public static boolean isShulkerBoxChecked(ItemStack stack) {
		return !ShulkerFixSettings.hopperShulkerStacking && isShulkerBox(stack);
	}

	public static boolean isShulkerBoxLimited(ItemStack stack) {
		return ShulkerFixSettings.overstackedShulkerSignalStrength && isShulkerBox(stack);
	}

	public static boolean isShulkerBox(ItemStack stack) {
		return stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
	}

	public static boolean hasCustomMaxStackSize(ItemStack stack) {
		int defaultStackSize = stack.getDefaultComponents().getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1);
		int currentStackSize = stack.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1);
		return defaultStackSize != currentStackSize;
	}

	public static Inventory wrapInventory(Inventory inventory) {
		return new WrapperInventory(inventory);
	}

	public static boolean isWrapped(Inventory inventory) {
		return inventory instanceof WrapperInventory;
	}

	public static boolean collectOneItem(Inventory inventory, ItemEntity itemEntity) {
		ItemStack itemStack = itemEntity.getStack().copyWithCount(1);
		ItemStack itemStack2 = HopperBlockEntity.transfer(null, inventory, itemStack, null);
		if (itemStack2.isEmpty()) {
			itemEntity.getStack().decrement(1);
			if (itemEntity.getStack().isEmpty()) {
				itemEntity.setStack(ItemStack.EMPTY);
				itemEntity.discard();
			}
			return true;
		}
		return false;
	}

	private record WrapperInventory(Inventory delegate) implements Inventory {
		@Override
		public int size() {
			return this.delegate.size();
		}

		@Override
		public boolean isEmpty() {
			return this.delegate.isEmpty();
		}

		@Override
		public ItemStack getStack(int slot) {
			return this.delegate.getStack(slot);
		}

		@Override
		public ItemStack removeStack(int slot, int amount) {
			return this.delegate.removeStack(slot, amount);
		}

		@Override
		public ItemStack removeStack(int slot) {
			return this.delegate.removeStack(slot);
		}

		@Override
		public void setStack(int slot, ItemStack stack) {
			this.delegate.setStack(slot, stack);
		}

		@Override
		public void markDirty() {
			this.delegate.markDirty();
		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return this.delegate.canPlayerUse(player);
		}

		@Override
		public void clear() {
			this.delegate.clear();
		}
	}
}