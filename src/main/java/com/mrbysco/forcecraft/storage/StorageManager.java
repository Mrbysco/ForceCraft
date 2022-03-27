package com.mrbysco.forcecraft.storage;

import com.mojang.datafixers.util.Pair;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.items.ForceBeltItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class StorageManager {
	public static boolean isServer() {
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
	}

	private static final BeltWSD blankBelts = new BeltWSD();
	private static final PackWSD blankPacks = new PackWSD();

	protected static BeltWSD cachedBelts;
	protected static PackWSD cachedPacks;

	public static BeltWSD getBelts() {
		if (isServer()) {
			if (cachedBelts == null)
				cachedBelts = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(BeltWSD::load, BeltWSD::new, BeltWSD.NAME);

			return cachedBelts;
		}
		//just in case the client calls this somehow...
		return blankBelts;
	}

	public static PackWSD getPacks() {
		if (isServer()) {
			if (cachedPacks == null)
				cachedPacks = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(PackWSD::load, PackWSD::new, PackWSD.NAME);

			return cachedPacks;
		}
		//just in case the client calls this somehow...
		return blankPacks;
	}

	public static BeltStorage getOrCreateBelt(ItemStack stack) {
		UUID uuid;
		BeltWSD data = getBelts();

		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("uuid")) {
			uuid = UUID.randomUUID();
			tag.putUUID("uuid", uuid);
		} else
			uuid = tag.getUUID("uuid");

		return data.getOrCreate(uuid);
	}

	public static Optional<BeltStorage> getBelt(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains("uuid")) {
			BeltWSD data = getBelts();
			UUID uuid = stack.getTag().getUUID("uuid");
			if (data.contains(uuid)) {
				return Optional.of(data.get(uuid));
			}
		}
		return Optional.empty();
	}

	public static Optional<BeltStorage> getBelt(UUID uuid) {
		BeltWSD data = getBelts();
		if (data.contains(uuid)) {
			return Optional.of(data.get(uuid));
		}
		return Optional.empty();
	}

	public static PackStorage getOrCreatePack(ItemStack stack) {
		UUID uuid;
		PackWSD data = getPacks();

		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("uuid")) {
			uuid = UUID.randomUUID();
			tag.putUUID("uuid", uuid);
		} else
			uuid = tag.getUUID("uuid");

		return data.getOrCreate(uuid);
	}

	public static Optional<PackStorage> getPack(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains("uuid")) {
			PackWSD data = getPacks();
			UUID uuid = stack.getTag().getUUID("uuid");
			if (data.contains(uuid)) {
				return Optional.of(data.get(uuid));
			}
		}
		return Optional.empty();
	}

	public static Optional<PackStorage> getPack(UUID uuid) {
		PackWSD data = getPacks();
		if (data.contains(uuid)) {
			return Optional.of(data.get(uuid));
		}
		return Optional.empty();
	}


	public static Optional<UUID> getUUID(ItemStack stack) {
		if (stack.hasTag()) {
			if (stack.getTag().contains("uuid"))
				return Optional.of(stack.getTag().getUUID("uuid"));
		}
		return Optional.empty();
	}

	public static LazyOptional<IItemHandler> getCapability(ItemStack stack) {
		if (isServer()) {
			if (stack.getOrCreateTag().contains("uuid")) {
				if (stack.getItem() instanceof ForceBeltItem) {
					UUID uuid = stack.getTag().getUUID("uuid");
					return getBelt(uuid).map(BeltStorage::getOptional).orElse(LazyOptional.empty());
				} else if (stack.getItem() instanceof ForcePackItem) {
					UUID uuid = stack.getTag().getUUID("uuid");
					return getPack(uuid).map(PackStorage::getOptional).orElse(LazyOptional.empty());
				}
			}
		}
		return LazyOptional.empty();
	}


	public static class PackWSD extends SavedData {
		public static final String NAME = Reference.MOD_ID + "_packs";

		protected final HashMap<UUID, PackStorage> PACKS = new HashMap<>();

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

		public static PackWSD load(CompoundTag nbt) {
			PackWSD data = new PackWSD();
			if (nbt.contains("packs")) {
				ListTag list = nbt.getList("packs", Tag.TAG_COMPOUND);
				list.forEach((invNBT) -> data.PACKS.put(((CompoundTag) invNBT).getUUID("uuid"), new PackStorage((CompoundTag) invNBT)));
			}
			return data;
		}

		@Override
		@Nonnull
		public CompoundTag save(CompoundTag compound) {
			ListTag packs = new ListTag();
			PACKS.forEach(((uuid, data) -> packs.add(data.toNBT())));
			compound.put("packs", packs);
			return compound;
		}
	}

	protected static class BeltWSD extends SavedData {
		public static final String NAME = Reference.MOD_ID + "_belts";

		protected final HashMap<UUID, BeltStorage> BELTS = new HashMap<>();

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

		public static BeltWSD load(CompoundTag nbt) {
			BeltWSD data = new BeltWSD();
			if (nbt.contains("belts")) {
				ListTag list = nbt.getList("belts", Tag.TAG_COMPOUND);
				list.forEach((invNBT) -> data.BELTS.put(((CompoundTag) invNBT).getUUID("uuid"), new BeltStorage((CompoundTag) invNBT)));
			}
			return data;
		}

		@Override
		@Nonnull
		public CompoundTag save(CompoundTag compound) {
			ListTag belts = new ListTag();
			BELTS.forEach(((uuid, data) -> belts.add(data.toNBT())));
			compound.put("belts", belts);
			return compound;
		}
	}
}
