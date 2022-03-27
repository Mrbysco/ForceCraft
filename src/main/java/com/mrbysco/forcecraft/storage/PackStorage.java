package com.mrbysco.forcecraft.storage;

import com.mrbysco.forcecraft.capabilities.pack.PackItemStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.UUID;

public class PackStorage {
	private final UUID uuid;
	private final PackItemStackHandler inventory;
	private final LazyOptional<IItemHandler> optional;

	public PackStorage(UUID uuid) {
		this.uuid = uuid;
		this.inventory = new PackItemStackHandler();
		this.optional = LazyOptional.of(() -> this.inventory);
	}

	public PackStorage(CompoundTag nbt) {
		this.uuid = nbt.getUUID("uuid");
		this.inventory = new PackItemStackHandler();
		this.inventory.deserializeNBT(nbt.getCompound("inventory"));
		this.optional = LazyOptional.of(() -> this.inventory);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public LazyOptional<IItemHandler> getOptional() {
		return this.optional;
	}

	public PackItemStackHandler getInventory() {
		return this.inventory;
	}

	public CompoundTag toNBT() {
		CompoundTag nbt = new CompoundTag();

		nbt.putUUID("uuid", this.uuid);
		nbt.putString("sUUID", this.uuid.toString());

		nbt.put("inventory", this.inventory.serializeNBT());

		return nbt;
	}
}
