package mrbysco.forcecraft.items.infuser;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class UpgradeBookData {
	private UpgradeBookTier tier = UpgradeBookTier.ZERO;
	int points = 0;
	
	public UpgradeBookData(ItemStack book) {
		if(book.getItem() != ForceRegistry.UPGRADE_TOME.get()) {
			ForceCraft.LOGGER.error("invalid book data entering book {}", book);
			return;
		}
		CompoundNBT tag = book.getOrCreateTag();
		if(tag.contains("tier")) {
			//is not empty, load it up
			this.read(book, tag);
		}
		//else its a new craft, or showing in JEI etc
	}
	//how many we need for next tier increment
	public int nextTier() {
		return getTier().pointsForLevelup() - points;
	}
	
	public void incrementPoints(int incoming) {
		points += incoming;
		if ( points >= this.getTier().pointsForLevelup()
				&& getTier() != UpgradeBookTier.FINAL) {
			//then go
			points -= this.getTier().pointsForLevelup();
			setTier(getTier().incrementTier());
		}
	}

	private void read(ItemStack book, CompoundNBT tag) {
		setTier(UpgradeBookTier.values()[tag.getInt("tier")]);
		points = tag.getInt("points");
		
	}

	public CompoundNBT write(ItemStack bookInSlot) {
		CompoundNBT tag = bookInSlot.getOrCreateTag();
		tag.putInt("tier", getTier().ordinal());
		tag.putInt("points", points);
		
		return tag;
	}
	public UpgradeBookTier getTier() {
		return tier;
	}
	public void setTier(UpgradeBookTier tier) {
		this.tier = tier;
	}
	
}