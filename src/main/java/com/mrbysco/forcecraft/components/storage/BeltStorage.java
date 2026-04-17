package com.mrbysco.forcecraft.components.storage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.UUID;

public class BeltStorage {
	private final UUID uuid;
	private final BeltStackHandler inventory;

	public BeltStorage(UUID uuidIn) {
		uuid = uuidIn;
		inventory = new BeltStackHandler();
	}

	public BeltStorage(CompoundTag tag, HolderLookup.Provider registries) {
		uuid = tag.getUUID("uuid");
		inventory = new BeltStackHandler();
		inventory.deserializeNBT(registries, tag.getCompound("inventory"));
	}

	public UUID getUUID() {
		return uuid;
	}

	public ResourceHandler<ItemResource> getInventory() {
		return inventory;
	}

	public CompoundTag toNBT(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		tag.putUUID("uuid", uuid);
		tag.put("inventory", inventory.serializeNBT(registries));
		return tag;
	}
}
