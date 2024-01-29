package com.mrbysco.forcecraft.attachment.storage;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.UUID;

public class BeltStorage {
	private final UUID uuid;
	private final BeltStackHandler inventory;

	public BeltStorage(UUID uuidIn) {
		uuid = uuidIn;
		inventory = new BeltStackHandler();
	}

	public BeltStorage(CompoundTag tag) {
		uuid = tag.getUUID("uuid");
		inventory = new BeltStackHandler();
		inventory.deserializeNBT(tag.getCompound("inventory"));
	}

	public UUID getUUID() {
		return uuid;
	}

	public IItemHandler getInventory() {
		return inventory;
	}

	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putUUID("uuid", uuid);
		tag.put("inventory", inventory.serializeNBT());
		return tag;
	}
}
