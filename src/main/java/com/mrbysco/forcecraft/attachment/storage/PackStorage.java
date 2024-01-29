package com.mrbysco.forcecraft.attachment.storage;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PackStorage {
	private final UUID uuid;
	private final PackStackHandler inventory;

	public PackStorage(UUID uuid) {
		this.uuid = uuid;
		this.inventory = new PackStackHandler();
	}

	public PackStorage(CompoundTag tag) {
		this.uuid = tag.getUUID("uuid");
		this.inventory = new PackStackHandler();
		this.inventory.deserializeNBT(tag.getCompound("inventory"));
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public PackStackHandler getInventory() {
		return this.inventory;
	}

	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();

		tag.putUUID("uuid", this.uuid);
		tag.putString("sUUID", this.uuid.toString());

		tag.put("inventory", this.inventory.serializeNBT());

		return tag;
	}
}
