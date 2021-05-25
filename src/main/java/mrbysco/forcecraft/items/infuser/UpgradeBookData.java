package mrbysco.forcecraft.items.infuser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.recipe.InfuseRecipe;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

public class UpgradeBookData {
	private UpgradeBookTier tier = UpgradeBookTier.ZERO;
	private Map<Integer, Set<ResourceLocation>> recipesUsed = new HashMap<>();
	private int points = 0;
	 
	public UpgradeBookData(ItemStack book) {
		if(book.getItem() != ForceRegistry.UPGRADE_TOME.get()) {
			ForceCraft.LOGGER.error("invalid book data entering book {}", book);
			return;
		}
		CompoundNBT tag = book.getTag();
		if(tag != null && tag.contains("tier")) {
			//is not empty, load it up
			this.read(book, tag);
		}
		//else its a new craft, or showing in JEI etc
	}
	//how many we need for next tier increment
	public int nextTier() {
		
		if(getTier() == UpgradeBookTier.FINAL) {
			return 0;
		}
		return getTier().pointsForLevelup() - points;
	}
	
	public void onRecipeApply(InfuseRecipe recipe) {
		Integer tier = recipe.getTier().ordinal();
		Set<ResourceLocation> tierSet = new HashSet<>();

		if(recipesUsed.containsKey(tier)) {
			tierSet = recipesUsed.get(tier);
		}
		
		tierSet.add(recipe.getId());
		recipesUsed.put(tier, tierSet); 
	}
	
	public void incrementPoints(int incoming) {
		points += incoming;
		if (canLevelUp()) {
			//then go
			
			
			points -= this.getTier().pointsForLevelup();
			setTier(getTier().incrementTier());
		}
	}
	private boolean canLevelUp() {
		if(points < this.getTier().pointsForLevelup()
			|| getTier() == UpgradeBookTier.FINAL) {
			return false;
		}
		
		// check more
		Set<ResourceLocation> thisTier = this.recipesUsed.get(this.tier.ordinal());
		int recipesThisTier = (thisTier == null) ? 0 : thisTier.size();
		
		ForceCraft.LOGGER.info("can lvlup? recipesThisTier = "+ recipesThisTier);
		
		return true;
	}

	private void read(ItemStack book, CompoundNBT tag) {
		setTier(UpgradeBookTier.values()[tag.getInt("tier")]);
		points = tag.getInt("points");

		for(UpgradeBookTier tier : UpgradeBookTier.values()) {
			Set<ResourceLocation> tierSet = new HashSet<>();
			
			ListNBT listTag = (ListNBT)tag.get("tier" + tier.ordinal()); // can be null TODO
			
			if(listTag != null) {
				for(int i = 0; i < listTag.size(); i++) {
					CompoundNBT tg = (CompoundNBT) listTag.get(i);
					String id = tg.getString("id");
					tierSet.add(ResourceLocation.tryCreate(id));
				}
			}
			recipesUsed.put(tier.ordinal(), tierSet);
		}
	}

	public CompoundNBT write(ItemStack bookInSlot) {
		CompoundNBT tag = bookInSlot.getOrCreateTag();
		tag.putInt("tier", getTier().ordinal());
		tag.putInt("points", points);
		
		
		for(UpgradeBookTier tier : UpgradeBookTier.values()) {
			Set<ResourceLocation> tierSet = recipesUsed.get(tier.ordinal());
			
			//save   
			ListNBT listTag = new ListNBT(); 
			CompoundNBT tg = new CompoundNBT();
			for(ResourceLocation id : tierSet) {
				tg.putString("id", id.toString());
			}
			listTag.add(tg);
			
			tag.put("tier" + tier.ordinal(), listTag);
		}
		
		return tag;
	}
	
	public UpgradeBookTier getTier() {
		return tier;
	}
	
	public void setTier(UpgradeBookTier tier) {
		this.tier = tier;
	}
	
	public int getPoints() {
		return points;
	}
	
}