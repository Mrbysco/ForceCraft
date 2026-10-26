package com.mrbysco.forcecraft.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class PackStorage {
	public static final Codec<PackStorage> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			UUIDUtil.STRING_CODEC.fieldOf("uuid").forGetter(PackStorage::getUUID),
			Codec.INT.optionalFieldOf("upgrades", 0).forGetter(storage -> storage.inventory.getUpgrades()),
			ItemStack.OPTIONAL_CODEC.listOf().optionalFieldOf("items", List.of())
					.forGetter(storage -> storage.inventory.getStacks())
	).apply(inst, PackStorage::new));

	private final UUID uuid;
	private final PackStackHandler inventory;

	public PackStorage(UUID uuid) {
		this.uuid = uuid;
		this.inventory = new PackStackHandler();
	}

	private PackStorage(UUID uuid, int upgrades, List<ItemStack> items) {
		this(uuid);
		this.inventory.setUpgrades(upgrades);
		this.inventory.setStacks(items);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public PackStackHandler getInventory() {
		return this.inventory;
	}
}
