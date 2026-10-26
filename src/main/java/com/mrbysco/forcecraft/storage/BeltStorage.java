package com.mrbysco.forcecraft.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;
import java.util.UUID;

public class BeltStorage {
	public static final Codec<BeltStorage> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			UUIDUtil.STRING_CODEC.fieldOf("uuid").forGetter(BeltStorage::getUUID),
			ItemStack.OPTIONAL_CODEC.listOf().optionalFieldOf("items", List.of())
					.forGetter(storage -> storage.inventory.getStacks())
	).apply(inst, BeltStorage::new));

	private final UUID uuid;
	private final BeltStackHandler inventory;

	public BeltStorage(UUID uuidIn) {
		uuid = uuidIn;
		inventory = new BeltStackHandler();
	}

	private BeltStorage(UUID uuid, List<ItemStack> items) {
		this(uuid);
		this.inventory.setStacks(items);
	}

	public UUID getUUID() {
		return uuid;
	}

	public ResourceHandler<ItemResource> getInventory() {
		return inventory;
	}
}
