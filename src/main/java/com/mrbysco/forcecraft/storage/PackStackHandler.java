package com.mrbysco.forcecraft.storage;

import com.mrbysco.forcecraft.item.ForcePackItem;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.List;

public class PackStackHandler extends ItemStacksResourceHandler {
	private static final int SLOTS_PER_UPGRADE = 8;
	private static final int MAX_UPGRADES = 4;
	private int upgrades;

	public PackStackHandler() {
		//always set handler to max slots, even if some are hidden/notused
		super((MAX_UPGRADES + 1) * SLOTS_PER_UPGRADE);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		//Make sure there's no ForcePack-ception
		return !(resource.getItem() instanceof ForcePackItem)
				&& !resource.is(ForceTags.Items.HOLDS_ITEMS)
				&& super.isValid(index, resource);
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
		this.upgrades = Math.clamp(this.upgrades, 0, MAX_UPGRADES);
		StorageManager.getPacks().setDirty();
	}

	@Override
	protected void onContentsChanged(int index, ItemStack previousContents) {
		StorageManager.getPacks().setDirty();
	}

	public List<ItemStack> getStacks() {
		return stacks.stream().map(ItemStack::copy).toList();
	}

	public void setStacks(List<ItemStack> list) {
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, i < list.size() ? list.get(i).copy() : ItemStack.EMPTY);
		}
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
}