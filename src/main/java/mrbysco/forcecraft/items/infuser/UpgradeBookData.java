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
import net.minecraft.util.ResourceLocation;

public class UpgradeBookData {
	private UpgradeBookTier tier = UpgradeBookTier.ZERO;
	private Map<Integer, ResourceLocation> recipeGroups = new HashMap<>();
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
		if(recipeGroups.isEmpty()) {
//			for( )
		}
//		if(!completedRecipes.contains(recipe.getId())) {
//			completedRecipes.add(recipe.getId());
//		}
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
	public int getPoints() {
		return points;
	}
	
}