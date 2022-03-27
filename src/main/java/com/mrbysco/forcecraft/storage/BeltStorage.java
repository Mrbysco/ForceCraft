package com.mrbysco.forcecraft.storage;

import com.mrbysco.forcecraft.items.ForceBeltItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BeltStorage {
	private final UUID uuid;
	private final BeltHandler inventory;
	private final LazyOptional<IItemHandler> optional;

	public BeltStorage(UUID uuidIn) {
		uuid = uuidIn;
		inventory = new BeltHandler(8);
		optional = LazyOptional.of(() -> inventory);
	}

	public BeltStorage(CompoundTag tag) {
		uuid = tag.getUUID("uuid");
		inventory = new BeltHandler(8);
		inventory.deserializeNBT(tag.getCompound("inventory"));
		optional = LazyOptional.of(() -> inventory);
	}

	public UUID getUUID() {
		return uuid;
	}

	public LazyOptional<IItemHandler> getOptional() {
		return optional;
	}

	public IItemHandler getInventory() {
		return inventory;
	}

	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putUUID("uuid", uuid);
		tag.putString("sUUID", uuid.toString());
		tag.put("inventory", inventory.serializeNBT());
		return tag;
	}


	private static class BeltHandler extends ItemStackHandler {
		public BeltHandler(int size) {
			super(size);
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return ForceBeltItem.filter(stack);
		}

		@Override
		protected void onContentsChanged(int slot) {
			StorageManager.getBelts().setDirty();
		}
	}
}
