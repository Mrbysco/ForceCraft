package mrbysco.forcecraft.capablilities.pack;

import mrbysco.forcecraft.items.ForcePackItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class PackItemStackHandler extends ItemStackHandler {
	private int upgrades;

	public PackItemStackHandler(int size) {
		super(size);
		this.upgrades = size % 8 == 0 ? size / 8 - 1 : 0;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		//Make sure there's no ForcePack-ception
		return !(stack.getItem() instanceof ForcePackItem) && super.isItemValid(slot, stack);
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

	public void applydowngrade() {
		this.upgrades--;
		forceUpdate();
	}

	public void forceUpdate() {
		CompoundNBT tag = serializeNBT();
		deserializeNBT(tag);
	}

	public boolean canUpgrade() {
		return this.upgrades < 4;
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
		nbt.putInt("Upgrades", upgrades);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setUpgrades(nbt.contains("Upgrades", Constants.NBT.TAG_INT) ? nbt.getInt("Upgrades") : upgrades);
		setSize((getUpgrades() + 1) * 8);

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