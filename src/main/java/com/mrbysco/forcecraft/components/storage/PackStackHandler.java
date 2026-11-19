package com.mrbysco.forcecraft.components.storage;

import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PackStackHandler extends ItemStackHandler {
	private static final int SLOTS_PER_UPGRADE = 8;
	private static final int MAX_UPGRADES = 4;
	public static final String NBT_UPGRADES = "Upgrades";
	private int upgrades;

	public PackStackHandler() {
		//always set handler to max slots, even if some are hidden/notused
		super((MAX_UPGRADES + 1) * SLOTS_PER_UPGRADE);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		//Make sure there's no ForcePack-ception
		return !(stack.getItem() instanceof ForcePackItem) && !stack.is(ForceTags.Items.HOLDS_ITEMS) && super.isItemValid(slot, stack);
	}

	public int getSlotsInUse() {
		return (upgrades + 1) * SLOTS_PER_UPGRADE;
	}

	public int getUpgrades() {
		return this.upgrades;
	}

	public void setUpgrades(int upgrades) {
		this.upgrades = upgrades;
	}

	public void applyUpgrade() {
		this.upgrades++;
		forceUpdate();
	}

	public void applyUpgrade(int upgrades) {
		this.upgrades += upgrades;
		forceUpdate();
	}

	public void applyDowngrade() {
		this.upgrades--;
		forceUpdate();
	}

	public void applyDowngrade(int upgrades) {
		this.upgrades -= upgrades;
		forceUpdate();
	}

	public void forceUpdate() {
		if (this.upgrades > MAX_UPGRADES) {
			this.upgrades = MAX_UPGRADES;
		}
		if (this.upgrades < 0) {
			this.upgrades = 0;
		}

		onContentsChanged(0);
		//CompoundTag tag = serializeNBT(); //TODO what in the world...
		//deserializeNBT(tag);
	}

	@Override
	protected void onContentsChanged(int slot) {
		StorageManager.getPacks().setDirty();
	}

	public boolean canUpgrade(UpgradeBookData bd) {
		if (upgrades >= MAX_UPGRADES) {
			return false;
		}
		if (bd.tier().asInt() >= UpgradeBookTier.TWO.asInt() && this.upgrades == 0) {
			//0->1 so 8 into 16 slots
			return true;
		}

		if (bd.tier().asInt() >= UpgradeBookTier.THREE.asInt() && this.upgrades == 1) {
			//about to become 24 slots
			return true;
		}

		if (bd.tier().asInt() >= UpgradeBookTier.FOUR.asInt() && this.upgrades == 2) {
			return true; // 32 slots next
		}

		if (bd.tier().asInt() >= UpgradeBookTier.FIVE.asInt() && this.upgrades == 3) {
			return true; // will be upgrade 4, 40 slots
		}

		return false;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		ListTag nbtTagList = new ListTag();
		for (int i = 0; i < stacks.size(); i++) {
			if (!stacks.get(i).isEmpty()) {
				CompoundTag itemTag = new CompoundTag();
				itemTag.putInt("Slot", i);
				nbtTagList.add(stacks.get(i).save(provider, itemTag));
			}
		}
		CompoundTag tag = new CompoundTag();
		tag.put("Items", nbtTagList);
		tag.putInt(NBT_UPGRADES, upgrades);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		setUpgrades(tag.contains(NBT_UPGRADES, CompoundTag.TAG_INT) ? tag.getInt(NBT_UPGRADES) : upgrades);

		ListTag tagList = tag.getList("Items", CompoundTag.TAG_COMPOUND);
		for (int i = 0; i < tagList.size(); i++) {
			CompoundTag itemTags = tagList.getCompound(i);
			int slot = itemTags.getInt("Slot");

			if (slot >= 0 && slot < stacks.size()) {
				stacks.set(slot, ItemStack.parseOptional(provider, itemTags));
			}
		}
		onLoad();
	}
}