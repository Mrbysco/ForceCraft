package mrbysco.forcecraft.capablilities.pack;

import mrbysco.forcecraft.items.ForcePackItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class PackItemStackHandler extends ItemStackHandler {
	private static final int SLOTS_PER_UPGRADE = 8;
	private static final int MAX_UPGRADES = 4;
	public static final String NBT_UPGRADES = "Upgrades";
	private int upgrades;

	public PackItemStackHandler() {
		//always set handler to max slots, evne if some are hidden/notused
		super((MAX_UPGRADES + 1) * SLOTS_PER_UPGRADE);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		//Make sure there's no ForcePack-ception
		return !(stack.getItem() instanceof ForcePackItem) && super.isItemValid(slot, stack);
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

	public void applydowngrade() {
		this.upgrades--;
		forceUpdate();
	}

	public void applydowngrade(int upgrades) {
		this.upgrades -= upgrades;
		forceUpdate();
	}

	public void forceUpdate() {
		if(this.upgrades > MAX_UPGRADES) {
			this.upgrades = MAX_UPGRADES;
		}
		if(this.upgrades < 0) {
			this.upgrades = 0;
		}
		CompoundNBT tag = serializeNBT();
		deserializeNBT(tag);
	}

	public boolean canUpgrade() {
		return this.upgrades < MAX_UPGRADES;
	}

	@Override
	public CompoundNBT serializeNBT() {
		ListNBT nbtTagList = new ListNBT();
		for (int i = 0; i < stacks.size(); i++) {
			if (!stacks.get(i).isEmpty()) {
				CompoundNBT itemTag = new CompoundNBT();
				itemTag.putInt("Slot", i);
				stacks.get(i).write(itemTag);
				nbtTagList.add(itemTag);
			}
		}
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("Items", nbtTagList);
		nbt.putInt(NBT_UPGRADES, upgrades);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setUpgrades(nbt.contains(NBT_UPGRADES, Constants.NBT.TAG_INT) ? nbt.getInt(NBT_UPGRADES) : upgrades);
//		setSize((getUpgrades() + 1) * 8);

		ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.size(); i++)
		{
			CompoundNBT itemTags = tagList.getCompound(i);
			int slot = itemTags.getInt("Slot");

			if (slot >= 0 && slot < stacks.size())
			{
				stacks.set(slot, ItemStack.read(itemTags));
			}
		}
		onLoad();
	}
}