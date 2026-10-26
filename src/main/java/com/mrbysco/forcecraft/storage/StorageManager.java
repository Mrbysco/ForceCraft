package com.mrbysco.forcecraft.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.item.ForceBeltItem;
import com.mrbysco.forcecraft.item.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StorageManager {
	public static boolean isServer() {
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
	}

	private static final BeltWSD blankBelts = new BeltWSD();
	private static final PackWSD blankPacks = new PackWSD();

	public static BeltWSD getBelts() {
		if (isServer()) {
			return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD)
					.getDataStorage().computeIfAbsent(BeltWSD.type());
		}
		//just in case the client calls this somehow...
		return blankBelts;
	}

	public static Optional<BeltStorage> getBelt(UUID uuid) {
		BeltWSD data = getBelts();
		if (data.contains(uuid)) {
			return Optional.of(data.get(uuid));
		}
		return Optional.empty();
	}

	public static BeltStorage getOrCreateBelt(ItemStack stack) {
		UUID uuid;
		BeltWSD data = getBelts();

		if (!stack.has(ForceComponents.UUID)) {
			uuid = UUID.randomUUID();
			stack.set(ForceComponents.UUID, uuid);
		} else {
			uuid = stack.get(ForceComponents.UUID);
		}

		return data.getOrCreate(uuid);
	}

	public static PackWSD getPacks() {
		if (isServer()) {
			return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD)
					.getDataStorage().computeIfAbsent(PackWSD.type());
		}
		//just in case the client calls this somehow...
		return blankPacks;
	}

	public static Optional<PackStorage> getPack(UUID uuid) {
		PackWSD data = getPacks();
		if (data.contains(uuid)) {
			return Optional.of(data.get(uuid));
		}
		return Optional.empty();
	}

	public static PackStorage getOrCreatePack(ItemStack stack) {
		UUID uuid;
		PackWSD data = getPacks();

		if (!stack.has(ForceComponents.UUID)) {
			uuid = UUID.randomUUID();
			stack.set(ForceComponents.UUID, uuid);
		} else {
			uuid = stack.get(ForceComponents.UUID);
		}

		return data.getOrCreate(uuid);
	}

	public static ResourceHandler<ItemResource> getCapability(ItemStack stack) {
		if (isServer()) {
			if (stack.has(ForceComponents.UUID)) {
				UUID uuid = stack.get(ForceComponents.UUID);
				if (stack.getItem() instanceof ForceBeltItem) {
					return getBelt(uuid).map(BeltStorage::getInventory).orElse(null);
				} else if (stack.getItem() instanceof ForcePackItem) {
					return getPack(uuid).map(PackStorage::getInventory).orElse(null);
				}
			}
		}
		return null;
	}

	public static class PackWSD extends SavedData {
		private static final Identifier NAME = Reference.modLoc("packs");

		public static final Codec<PackWSD> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				PackStorage.CODEC.listOf().optionalFieldOf("packs", List.of())
						.forGetter(data -> List.copyOf(data.PACKS.values()))
		).apply(inst, PackWSD::new));

		protected final HashMap<UUID, PackStorage> PACKS = new HashMap<>();

		public PackWSD() {
		}

		public PackWSD(List<PackStorage> packs) {
			packs.forEach(pack -> this.PACKS.put(pack.getUUID(), pack));
		}

		public static SavedDataType<PackWSD> type() {
			return new SavedDataType<>(NAME, PackWSD::new, CODEC, null);
		}

		public HashMap<UUID, PackStorage> get() {
			return PACKS;
		}

		public boolean contains(UUID uuid) {
			return PACKS.containsKey(uuid);
		}

		public PackStorage get(UUID uuid) {
			return PACKS.get(uuid);
		}

		public PackStorage getOrCreate(UUID uuid) {
			return PACKS.computeIfAbsent(uuid, id -> {
				setDirty();
				return new PackStorage(uuid);
			});
		}
	}

	public static class BeltWSD extends SavedData {
		private static final Identifier NAME = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "belts");

		public static final Codec<BeltWSD> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				BeltStorage.CODEC.listOf().optionalFieldOf("belts", List.of())
						.forGetter(data -> List.copyOf(data.BELTS.values()))
		).apply(inst, BeltWSD::new));

		protected final HashMap<UUID, BeltStorage> BELTS = new HashMap<>();

		public BeltWSD() {
		}

		public BeltWSD(List<BeltStorage> belts) {
			belts.forEach(belt -> this.BELTS.put(belt.getUUID(), belt));
		}

		public static SavedDataType<BeltWSD> type() {
			return new SavedDataType<>(NAME, BeltWSD::new, CODEC, null);
		}

		public HashMap<UUID, BeltStorage> get() {
			return BELTS;
		}

		public boolean contains(UUID uuid) {
			return BELTS.containsKey(uuid);
		}

		public BeltStorage get(UUID uuid) {
			return BELTS.get(uuid);
		}

		public BeltStorage getOrCreate(UUID uuid) {
			return BELTS.computeIfAbsent(uuid, id -> {
				setDirty();
				return new BeltStorage(uuid);
			});
		}
	}
}
